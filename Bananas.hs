{-# LANGUAGE TypeSynonymInstances, FlexibleInstances #-}

newtype Mu f = In { out :: (f (Mu f)) }

data Natf x = Zero | Succ x  deriving Show

type Natural  = Mu Natf

instance Show Natural where
  show (In Zero) = "In Zero"
  show (In (Succ x)) = "In (Succ " ++ show x ++ ")"
  
add :: Natural -> Natural -> Natural
add (In Zero) x = x
add x (In Zero) = x
add (In (Succ x)) (In (Succ x')) = In (Succ (In (Succ (add x x'))))
                                       

instance Functor Natf where
  fmap f (Zero) = Zero
  fmap f (Succ x) = Succ (f x)
  

intalgebra :: Natf Int -> Int
intalgebra Zero     = 0
intalgebra (Succ x) = 1 + x

intcoalgebra :: Int -> Natf Int
intcoalgebra 0 = Zero
intcoalgebra n = Succ (n - 1)

-- catamorphims are dse
cata :: Functor f => (f a -> a) -> (Mu  f -> a)
cata h = h . fmap (cata h) . out

ana :: Functor f => (a -> f a) -> (a -> Mu f)
ana h = In . fmap (ana h) . h

-- Standard recursive ADT definition
-- data Expr = Num Int | Add Expr Expr

-- catamorphisms on simple expression trees
data E e = Num Int | Add e e deriving Show
  
instance Functor E where
  fmap g =  \x -> case x of 
    Num n -> Num n
    Add e e' -> Add (g e) (g e')

type Expr = Mu  E

eval' (Num n)    = id n
eval' (Add e e') = e + e'

eval = cata eval'

-- list type
-- second parameter makes explicit the fact the actual type is a fixed point
-- of some equation L(x) = 1 + L(x)
data L a l = Nil | L a l deriving Show 

type List a = Mu  (L a)

-- This is the functor instance for the type L a, *not* for the usual List 
-- structure
instance Functor (L a) where
  fmap g Nil     = Nil
  fmap g (L x l) = L x (g l)
  
-- define non-recursive length function
len :: L a Int -> Int
len Nil      = 0
len (L a l)  = 1 + l

-- then apply cata to recurse in list
length = cata len

-- other example with filter
filter' :: (a -> Bool) -> L a (Mu  (L a)) -> Mu  (L a)
filter' p Nil                 = In Nil
filter' p (L a l) | p a       = In (L a l)
                  | otherwise = l

filter p = cata $ filter' p
