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
import Control.Arrow((***))
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

data PollStatus a = PollStatus { 
  quorum :: [ PriestId ],  -- ^Set of priests forming the quorum for current ballot
  voters :: [ PriestId ],  -- ^Set of quorum members from which that priest has received @Voted@ message
  decree :: a              -- ^Current decree 
  } deriving (Eq, Show, Ord)

data PriestLog a = PriestLog {
  outcome        :: Maybe a,      -- ^Outcome of current voting round
  lastTried      :: Ballot,       -- ^Number of last ballot that priest tried to begin
  previousBallot :: Maybe Ballot, -- ^Number of last ballot that priest voted in
  previousDecree :: Maybe a,      -- ^Decree last voted for
  nextBallot     :: Maybe Ballot  -- ^Number of last ballot that priest agreed to participate in
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

tryNewBallot :: Priest a -> (Priest a, Maybe (Message a))
tryNewBallot (Priest maxp myId log status votes poll) = 
  (Priest maxp 
   myId
   log { lastTried = nextbal }
   Trying
   emptyVotes
   poll, Nothing)
 where
   nextbal = myNextBallot maxp (lastTried log) myId

sendNextBallot :: Priest a -> (Priest a, Maybe (Message a))
sendNextBallot priest 
  | priestStatus priest == Trying = (priest, Just $ Message Nothing $ NextBallot (lastTried $ priestLog priest))
  | otherwise                     = (priest, Nothing)

sendLastVote :: Priest a -> (Priest a, Maybe (Message a))
sendLastVote priest@(Priest max pid _ _ _ _)
  | nextBal priest > prevBal priest = (priest, Just $ Message
                                               (Just (owner max $ fromJust $ nextBal priest))
                                               (LastVote (fromJust$nextBal priest) (Vote pid (prevBal priest) (prevDec priest))))
  | otherwise                       = (priest, Nothing)


startPolling :: a -> Priest a -> (Priest a, Maybe (Message a))
startPolling anyVote priest@(Priest max pid _ _ _ _) 
  = if priestStatus priest == Trying && quorumReached  then
      (priest { priestStatus = Polling
              , pollStatus = (pollStatus priest)  { quorum = majoritySet
                                                  , voters = [] 
                                                  , decree = maxVote}}
      , Nothing)
    else
      (priest, Nothing)
  where
    votingPriests = nub $ map emitter (prevVotes priest)
    quorumReached = length votingPriests >= (max `div` 2 ) + 1
    majoritySet   = take (max `div` 2 + 1) votingPriests
    maxVote       = fromMaybe anyVote $ decreeNumber $ head $ reverse $ sortBy (comparing ballotNumber) (prevVotes priest)

sendBeginBallot :: Priest a -> (Priest a, Maybe (Message a))
sendBeginBallot priest@(Priest max pid _ Polling _ _) = (priest, Just $ Message
                                                                 (Just $ select $ quorum $ pollStatus priest)
                                                                 (BeginBallot (lastTried $ priestLog priest) (decree $ pollStatus priest)))
sendBeginBallot priest                                = (priest, Nothing)                                                                  

sendVoted :: Priest a -> (Priest a, Maybe (Message a))
sendVoted priest@(Priest max pid _ _ _ _) 
  | prevBal priest /= Nothing = (priest, Just $ Message
                                         (Just $ owner max $ fromJust $ prevBal priest)
                                         (Voted (fromJust $ prevBal priest) pid))
  | otherwise                 = (priest, Nothing)

succeed :: (Eq a) => Priest a -> (Priest a, Maybe (Message a))
succeed priest@(Priest max pid _ Polling _ poll) = 
  if (all (`elem` (voters poll)) (quorum poll)) && (outcome (priestLog priest) == Nothing) then
    (priest { priestLog = (priestLog priest) { outcome = Just $ decree (pollStatus priest) }}, Nothing)
  else
    (priest, Nothing)
succeed priest  =   (priest, Nothing)
  
sendSuccess :: (Eq a) => Priest a -> (Priest a, Maybe (Message a))
sendSuccess priest | Just result <- outcome (priestLog priest) = (priest, Just $ Message Nothing (Success result))
                   | otherwise                                 = (priest, Nothing)
    
    
receive :: (Eq a) => MessageBody a -> Priest a -> Priest a
receive (NextBallot ballot)    priest   = if ballot >= (fromMaybe (-2^31) $ nextBal priest) then
                                            priest { priestLog = (priestLog priest) { nextBallot = Just ballot}}
                                          else
                                            priest
receive (LastVote ballot vote) priest   = if priestStatus priest == Trying && ballot == lastTried (priestLog priest) then
                                            priest { prevVotes = prevVotes priest `union` [vote] }
                                          else   
                                            priest
receive (BeginBallot ballot dec) priest = if ballot == (fromJust$ nextBal priest) && ballot > (fromJust$ prevBal priest) then
                                            priest { priestLog = (priestLog priest) { previousBallot = Just ballot 
                                                                                    , previousDecree = Just dec }}
                                          else 
                                            priest
receive (Voted ballot pid)       priest = if ballot == lastTried (priestLog priest) && priestStatus priest == Polling then 
                                            priest { pollStatus = pstatus { voters = (voters pstatus) `union` [pid] }}
                                          else
                                            priest
  where
    pstatus = pollStatus priest
receive (Success decree)        priest = if outcome (priestLog priest) == Nothing then
                                           priest { priestLog = (priestLog priest) { outcome = Just decree }}
                                         else
                                           priest

-- Execution of a session of the paxos algorithm for a number of priests

action :: (Priest a -> (Priest a, Maybe (Message a))) 
          -> Maybe (MessageBody a) 
          -> Priest a
          -> (Priest a, (Maybe (Message a), Maybe (MessageBody a)))
action act input priest = (priest', (output,input)) 
  where
    (priest',output) = act priest
    
receiveAct :: (Show a, Eq a) => 
              Maybe (MessageBody a) 
              -> Priest a
              -> (Priest a, (Maybe (Message a), Maybe (MessageBody a)))
receiveAct (Just input) priest = let priest' = receive input priest
                                 in  if priest == priest' then
                                       (priest, (Nothing, Just input))
                                     else
                                       trace ("received message " ++ show input) $ (priest', (Nothing, Nothing))
receiveAct Nothing      priest = (priest, (Nothing, Nothing))

actions = ("receive",receiveAct) : map (id *** action) [ ("try new ballot", tryNewBallot)
                                                       , ("sendNextBallot",sendNextBallot)
                                                       , ("sendLastVote",sendLastVote)
                                                       , ("startPolling",startPolling 1)
                                                       , ("sendBeginBallot",sendBeginBallot)
                                                       , ("sendVoted",sendVoted)
                                                       , ("succeed",succeed)
                                                       , ("sendSuccess",sendSuccess) ]

parliament :: Int -> [Priest Int]
parliament numberOfPriests = map (idlePriest numberOfPriests) [0 .. numberOfPriests -1]

idlePriest max id = Priest max id (PriestLog Nothing 0 Nothing Nothing  Nothing) Idle [] (PollStatus [] [] 0)

initializeMbox :: Priest Int -> (Priest Int, [MessageBody Int])
initializeMbox = (,[])

stepParliament :: [(Priest Int,[MessageBody Int])] ->  [(Priest Int,[MessageBody Int])]
stepParliament priests = dispatchMessages $ map stepPriest priests

dispatchMessages actionResults = let max = length actionResults
                                     
                                     collectMessages  (ps,mms) (p,(ms,Just m))  = ((p,ms):ps, m:mms)
                                     collectMessages  (ps,mms) (p,(ms,Nothing)) = ((p,ms):ps, mms)
                                     
                                     allocateMessages priests messages = foldl allocateMessage (reverse priests) messages
                                     
                                     fillMBox pid mbody (priest,ms) | (priestId priest == pid) = (priest, ms ++ [mbody])
                                                                    | otherwise                = (priest, ms)
                                                                                       
                                     allocateMessage priests (Message Nothing mbody)    = map (fillMBox (random max) mbody) priests
                                     allocateMessage priests (Message (Just pid) mbody) = map (fillMBox pid mbody) priests
                                     
                                 in uncurry allocateMessages $ foldl collectMessages ([],[]) actionResults

random :: Int -> Int
random m = unsafePerformIO $ randomRIO (0,m-1)

randomlySelectOneOf :: [(String,a)] -> a
randomlySelectOneOf xs = snd sel
  where
    sel = select xs

select xs = xs !! random (length xs)
  
stepPriest :: (Priest Int, [MessageBody Int]) -> (Priest Int, ([MessageBody Int], Maybe (Message Int)))
stepPriest (priest, message:messages) = let futures = map (id *** (($priest).($ (Just message)))) actions
                                        in case randomlySelectOneOf futures of
                                          (priest', (Nothing,Just _)) -> (priest',(messages, Nothing))
                                          (priest', (output,_))       -> (priest',(message:messages,output))
stepPriest (priest, [])               = let futures = map (id *** (($priest).($ Nothing))) actions
                                        in case randomlySelectOneOf futures of
                                          (priest', (output,Nothing))  -> (priest',([],output))