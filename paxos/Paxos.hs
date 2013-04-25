{-# LANGUAGE TupleSections #-}

{- 
A haskell implementation of the Paxos parliament protocol as exposed in 
 
 "The Part-Time Parliament", L.Lamport,  ACM TCS 16/2, May 1998

This protocol defines a way for independent asynchronous processes to reach agreement
on "decrees" in a decentralized and fault-tolerant way, in order to ensure that
the following properties are guaranteed (under certain failure assumptions):

  * ''Consistency'': All "ledgers" (record of all passed decrees) are consistent: No two ledgers can contain
    contradictory information
  
  * ''Progress'': Given a stable number of processes for a given period of time, all proposed
    decrees have been passed and recorded in all ledgers of participating processes.

It is based ultimately on the "synod protocol" which describes a protocol for passing a single decree (a ballot)
and define some needed properties to ensure global consistency and progress:

  * Each ballot has a unique ballot number

  * The quorums of any two ballots have at least one process in common

  * For any ballot B, if any process in B's quorum voted in an earlier ballot, then the decree of B 
    must be equal to the latest of those earlier ballots

Note that this protocol handle the case of concurrent ballots occuring.

 -}
module Paxos where

import Data.List(union, nub, sortBy)
import Data.Maybe(fromMaybe, fromJust)
import Data.Ord(comparing)
import System.IO.Unsafe(unsafePerformIO)
import System.Random(randomRIO)
import Debug.Trace
import Control.Arrow((***),second)
type Ballot = Int

type PriestId = Int

-- |A Single vote
data Vote a  = Vote { emitter :: PriestId
                    , ballotNumber :: Maybe Ballot
                    , decreeNumber :: Maybe a
                    } deriving (Eq, Show, Ord, Read)

data PriestStatus = 
  Idle   |  -- ^Priest is not doing anything, initial state
  Trying |  -- ^Priest is trying to vote in @lastTried@ ballot number
  Polling   -- ^Priest is currently taking part in @lastTried@ ballot number
  deriving (Eq, Show, Ord, Enum)
           
type Votes a = [Vote a]
emptyVotes = []

-- |Poll status represents transient information that can be lost by the priest.
-- 
-- In the original paper by Lamport, this is the "slip of paper" priests use to record
-- information that can be lost.
data PollStatus a = PollStatus { 
  quorum :: [ PriestId ],  -- ^Set of priests forming the quorum for current ballot
  voters :: [ PriestId ],  -- ^Set of quorum members from which that priest has received @Voted@ message
  decree :: a              -- ^Current decree 
  } deriving (Eq, Show, Ord)

-- |Priest's log represents persistent data that is kept in his/her "ledger".
--
data PriestLog a = PriestLog {
  outcome        :: Maybe a,      -- ^Outcome of current voting round
  lastTried      :: Ballot,       -- ^Number of last ballot that priest tried to initiate
  nextBallot     :: Maybe Ballot, -- ^Number of last ballot that priest agreed to participate in
  previousBallot :: Maybe Ballot, -- ^Number of last ballot that priest voted in
  previousDecree :: Maybe a       -- ^Decree last voted for
 } deriving (Eq, Show, Ord)
                

data Priest a = Priest {
  maxPriest    :: Int,
  priestId     :: PriestId,
  priestLog    :: PriestLog a,
  priestStatus :: PriestStatus,
  prevVotes    :: Votes a,
  pollStatus   :: PollStatus a
  } deriving (Eq, Show, Ord)

-- |Partition for set of ballots
--
-- Each priest is the owner of a particular subset of all possible ballots. 
-- This is simply a modulus on the maximum number of priests.
owner :: Int -> Ballot -> PriestId
owner m b = b `mod` m

-- |Next ballot number for the given priest.
myNextBallot :: Int          -- ^Maximum number of Priests
                -> Ballot    -- ^current ballot number (maybe for this priest or another)
                -> PriestId  -- ^Interested priest
                -> Ballot    -- ^Next computed ballot number for given priest
myNextBallot m n p = ((n `div` m + 1) * m) + p

nextBal = nextBallot.priestLog
prevBal = previousBallot.priestLog
prevDec = previousDecree.priestLog

-- Here starts the set of messages exchanged by priests and how they change
-- state
data Message a = Message 
                 (Maybe PriestId)  -- ^If Nothing, then this message should be sent to any priest
                 (MessageBody a)
  deriving (Eq, Show, Ord, Read)
  

data MessageBody a = 
  NextBallot Ballot |
  LastVote Ballot (Vote a) |
  BeginBallot Ballot a |
  Voted Ballot PriestId |
  Success a
  deriving (Eq, Show, Ord, Read)

negInf = (-2^31)

type ActionResult a = (Priest a, Maybe (Message a), String)

idle :: (Show a) => Priest a -> ActionResult a
idle p = (p,Nothing,"idling")

tryNewBallot :: (Show a) => Priest a -> ActionResult a
tryNewBallot priest@(Priest maxp myId log status votes poll)
  | myId == 0 && priestStatus priest /= Trying && priestStatus priest /= Polling = (Priest maxp myId (log { lastTried = nextbal }) Trying emptyVotes poll, 
                                                                Nothing,
                                                                "trying new ballot " ++ (show nextbal))
  | otherwise                     = (priest,Nothing,"")
 where
   nextbal = myNextBallot maxp (lastTried log) myId

sendNextBallot :: (Show a) => Priest a -> ActionResult a
sendNextBallot priest 
  | priestStatus priest == Trying = (priest, Just $ Message Nothing $ NextBallot tried, "sending nextBallot " ++ (show tried))
  | otherwise                     = (priest, Nothing, "")
  where tried = (lastTried $ priestLog priest)
        
sendLastVote :: (Show a) => Priest a -> ActionResult a
sendLastVote priest@(Priest max pid _ _ _ _)
  | nextBal priest > prevBal priest = (priest, Just $ Message
                                               (Just (owner max $ fromJust $ nextBal priest))
                                               lastvote,"sending last vote " ++ (show lastvote))
  | otherwise                       = (priest, Nothing, "")
 where 
   lastvote = (LastVote (fromJust$nextBal priest) (Vote pid (prevBal priest) (prevDec priest)))


startPolling :: (Show a) => a -> Priest a -> ActionResult a
startPolling anyVote priest@(Priest max pid _ _ _ _) 
  = if priestStatus priest == Trying && quorumReached  then
      (priest { priestStatus = Polling
              , pollStatus = (pollStatus priest)  { quorum = majoritySet
                                                  , voters = [] 
                                                  , decree = maxVote}}
      , Nothing
      , "start polling decree " ++ (show maxVote))
    else
      (priest, Nothing,"")
  where
    votingPriests = nub $ map emitter (prevVotes priest)
    quorumReached = length votingPriests >= (max `div` 2 ) + 1
    majoritySet   = take (max `div` 2 + 1) votingPriests
    maxVote       = fromMaybe anyVote $ decreeNumber $ head $ reverse $ sortBy (comparing ballotNumber) (prevVotes priest)

sendBeginBallot :: (Show a) => Priest a -> ActionResult a
sendBeginBallot priest@(Priest max pid _ Polling _ _) = (priest, Just $ Message
                                                                 (Just $ select $ quorum $ pollStatus priest)
                                                                 (BeginBallot last dec)
                                                        , "sending begin ballot "++ (show last) ++"," ++ (show dec))
  where
    last = (lastTried $ priestLog priest)
    dec  = (decree $ pollStatus priest)
sendBeginBallot priest                                = (priest, Nothing, "")                                                                  
    

sendVoted :: (Show a) => Priest a -> ActionResult a
sendVoted priest@(Priest max pid _ _ _ _) 
  | prevBal priest /= Nothing = (priest, Just $ Message
                                         (Just $ owner max $ fromJust $ prevBal priest)
                                         (Voted bal pid)
                                ,"sending voted "++ (show bal))
  | otherwise                 = (priest, Nothing,"")
 where
   bal = (fromJust $ prevBal priest)
   
succeed :: (Eq a,Show a) => Priest a -> ActionResult a
succeed priest@(Priest max pid _ Polling _ poll) = 
  if (all (`elem` (voters poll)) (quorum poll)) && (outcome (priestLog priest) == Nothing) then
    (priest { priestLog = (priestLog priest) { outcome = Just $ dec }}, Nothing
    ,"succeeding decree "++ (show dec))
  else
    (priest, Nothing ,"")
  where 
    dec = decree (pollStatus priest) 
succeed priest  =   (priest, Nothing,"")
  
sendSuccess :: (Eq a, Show a) => Priest a -> ActionResult a
sendSuccess priest | Just result <- outcome (priestLog priest) = (priest, Just $ Message Nothing (Success result), "sending success " ++ (show result))
                   | otherwise                                 = (priest, Nothing, "")
    
    
receive :: (Show a, Eq a) => MessageBody a -> Priest a -> (Priest a, String)
receive n@(NextBallot ballot)    priest   = if ballot >= (fromMaybe (-2^31) $ nextBal priest) then
                                            (priest { priestLog = (priestLog priest) { nextBallot = Just ballot}}
                                             , "receiving " ++ show n)
                                          else
                                            (priest,"")
receive l@(LastVote ballot vote) priest   = if priestStatus priest == Trying && ballot == lastTried (priestLog priest) then
                                            (priest { prevVotes = prevVotes priest `union` [vote] }
                                             , "receiving " ++ show l)
                                          else   
                                            (priest,"")
receive b@(BeginBallot ballot dec) priest = if ballot == (fromJust$ nextBal priest) &&  (maybe True (ballot >) (prevBal priest)) then
                                            (priest { priestLog = (priestLog priest) { previousBallot = Just ballot 
                                                                                     , previousDecree = Just dec }}
                                             ,"receiving "++ (show b))
                                          else 
                                            (priest,"")
receive v@(Voted ballot pid)       priest = if ballot == lastTried (priestLog priest) && priestStatus priest == Polling then 
                                            (priest { pollStatus = pstatus { voters = (voters pstatus) `union` [pid] }}
                                             ,"receiving "++ (show v))
                                          else
                                            (priest,"")
  where
    pstatus = pollStatus priest

receive s@(Success decree)        priest = if outcome (priestLog priest) == Nothing then
                                           (priest { priestLog = (priestLog priest) { outcome = Just decree }}
                                            ,"receiving success "++ (show s))
                                         else
                                           (priest,"")

-- Execution of a session of the paxos algorithm for a number of priests

action :: (Priest a -> ActionResult a) 
          -> Maybe (MessageBody a) 
          -> Priest a
          -> (Priest a, (Maybe (Message a), Maybe (MessageBody a)), String)
action act input priest = (priest', (output,input), log) 
  where
    (priest',output,log) = act priest
    
receiveAction :: (Show a, Eq a) => 
              Maybe (MessageBody a) 
              -> Priest a
              -> (Priest a, (Maybe (Message a), Maybe (MessageBody a)), String)
receiveAction (Just input) priest = let (priest', log) = receive input priest
                                 in  if priest == priest' then
                                       (priest, (Nothing, Just input), log)
                                     else
                                       (priest', (Nothing, Nothing), log)
receiveAction Nothing      priest = (priest, (Nothing, Nothing),"")

actions = receiveAction : receiveAction : receiveAction : receiveAction : receiveAction : receiveAction : receiveAction : map action [ idle
                                  , tryNewBallot
                                  , sendNextBallot
                                  , sendLastVote
                                  , startPolling 1
                                  , sendBeginBallot
                                  , sendVoted
                                  , succeed
                                  , sendSuccess ]

parliament :: Int -> [Priest Int]
parliament numberOfPriests = map (idlePriest numberOfPriests) [0 .. numberOfPriests -1]

idlePriest max id = Priest max id (PriestLog Nothing 0 Nothing Nothing  Nothing) Idle [] (PollStatus [] [] 0)

initializeMbox :: Priest Int -> (Priest Int, [MessageBody Int])
initializeMbox = (,[])

stepParliament :: [(Priest Int,[MessageBody Int])] ->  [(Priest Int,[MessageBody Int])]
stepParliament priests = dispatchMessages $ map stepPriest priests

dispatchMessages :: [(Priest a, ([MessageBody t], Maybe (Message t)))]
                    -> [(Priest a, [MessageBody t])]
dispatchMessages actionResults = let max = length actionResults
                                     
                                     collectMessages  (ps,mms) (p,(ms,Just m))  = ((p,ms):ps, (priestId p,m):mms)
                                     collectMessages  (ps,mms) (p,(ms,Nothing)) = ((p,ms):ps, mms)
                                     
                                     allocateMessages priests messages = foldl allocateMessage (reverse priests) messages
                                     
                                     fillMBox pid mbody (priest,ms) | (priestId priest == pid) = (priest, ms ++ [mbody])
                                                                    | otherwise                = (priest, ms)
                                                                                       
                                     allocateMessage priests (senderId, (Message Nothing mbody))    = map (fillMBox (randomExcept max senderId) mbody) priests
                                     allocateMessage priests (senderId, (Message (Just pid) mbody)) = map (fillMBox pid mbody) priests
                                     
                                 in uncurry allocateMessages $ foldl collectMessages ([],[]) actionResults

randomExcept :: Int -> Int -> Int 
randomExcept bound except = randomExcept' bound except (random bound)
  where
    randomExcept' b x n | x /= n    = n
                        | otherwise = randomExcept' b x (random bound)

random :: Int -> Int
random m = unsafePerformIO $ randomRIO (0,m-1)

randomlySelectOneOf :: [a] -> a
randomlySelectOneOf xs = select xs

select xs = xs !! random (length xs)

stepPriest :: (Priest Int, [MessageBody Int]) -> (Priest Int, ([MessageBody Int], Maybe (Message Int)))
stepPriest (priest, message:messages) = let futures = possibleFutures priest $ Just message
                                        in case randomlySelectOneOf futures of
                                          (priest', (Nothing,Just _),log)  -> trace (show (priestId priest') ++ " " ++ log) $ (priest',(messages, Nothing))
                                          (priest', (output,_)      ,log)  -> trace (show (priestId priest') ++ " " ++ log) $ (priest',(message:messages,output))
stepPriest (priest, [])               = let futures = possibleFutures priest Nothing
                                        in case randomlySelectOneOf futures of
                                          (priest', (output,Nothing),log)  -> trace (show (priestId priest') ++ " " ++ log) $ (priest',([],output))

possibleFutures priest m = filter effectiveActions $ map (($ priest).($ m)) actions
  where
    effectiveActions (_,_,s) = s /= ""
