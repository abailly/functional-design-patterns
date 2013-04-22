module PaxosTest where

import Test.HUnit
import Test.QuickCheck
import Control.Applicative
import Paxos
import Data.Maybe(fromMaybe)
import Data.List(nub)

data Configuration = Conf Int Ballot PriestId
                     deriving (Eq, Show, Ord)
                              
instance Arbitrary Configuration where
  arbitrary = do
    max    <- elements [ 2 .. 10 ]
    cur    <- elements [ 1 .. 1000000]
    priest <- elements [ 0 .. max-1 ]
    return $ Conf max cur priest
  
instance Arbitrary PriestStatus where
  arbitrary = toEnum <$> choose (0,2)
    
instance (Arbitrary a, Eq a) => Arbitrary (Vote a) where
  arbitrary = Vote <$> arbitrary <*> arbitrary <*> arbitrary
  
aPriestNumber max = choose (0, max-1)

genPollStatus :: (Arbitrary a) => Int -> Gen (PollStatus a)
genPollStatus maxNumPriest = do
  countQuorum <- choose (1,maxNumPriest)
  quorum <- vectorOf countQuorum (aPriestNumber maxNumPriest)
  countVoters <- choose (1,maxNumPriest)
  voters <- vectorOf countVoters (aPriestNumber maxNumPriest)
  decree <- arbitrary
  return $ PollStatus (nub quorum) (nub voters) decree
  
instance (Arbitrary a) => Arbitrary (PriestLog a) where
  arbitrary = PriestLog <$> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary
  
instance (Arbitrary a, Eq a) => Arbitrary (Priest a) where
  arbitrary = do
    countVotes <- choose (0,10)
    max      <- elements [ 2 .. 10 ]
    priestId <- elements [ 0 .. max-1 ]
    priestLog <- arbitrary
    status <- arbitrary
    votes <- vectorOf countVotes arbitrary
    pollStatus <- genPollStatus max
    return $ Priest max priestId priestLog status (nub votes) pollStatus
      
prop_ballot_numbers_are_unique_to_a_priest :: Configuration -> Property
prop_ballot_numbers_are_unique_to_a_priest (Conf max current priest) =
  forAll (elements $ filter (/= priest) [ 0 .. max-1])  $ 
  \otherPriest -> 
  myNextBallot max current priest /= myNextBallot max current otherPriest 

prop_owner_and_next_ballot_are_consistent :: Configuration -> Bool
prop_owner_and_next_ballot_are_consistent (Conf max current priest) =
  (owner max (myNextBallot max current priest) == priest)

prop_try_new_ballot_is_greater_than_previous (Conf max current priest) =
  (lastTried.priestLog.fst) (tryNewBallot newPriest) > lastTried (priestLog newPriest)
  where
    newPriest = (idlePriest 10 priest)
  
prop_update_next_ballot_when_greater_than_own_next_ballot :: Int -> Priest Int -> Bool
prop_update_next_ballot_when_greater_than_own_next_ballot ballot priest =  
  let priest'       = receive (NextBallot ballot) priest
      curNextBallot = fromMaybe (-2^31) $ nextBal priest
  in if ballot >=  curNextBallot then
       nextBal priest' == Just ballot
     else
       nextBal priest' == Just curNextBallot

