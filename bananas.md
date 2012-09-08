 - http://stackoverflow.com/questions/3632999/implementing-filter-using-hof-in-haskell

~~~~~~~~~ {.haskell .numberLines}
newtype Mu f = In { out :: (f (Rec f)) }

-- catamorphims are dse
cata :: Functor f => (f a -> a) -> (Rec f -> a)
cata h = h . fmap (cata h) . out

-- Standard recursive ADT definition
-- data Expr = Num Int | Add Expr Expr

-- catamorphisms on simple expression trees
data E e = Num Int | Add e e deriving Show
  
instance Functor E where
  fmap g =  \x -> case x of 
    Num n -> Num n
    Add e e' -> Add (g e) (g e')

type Expr = Rec E

eval' (Num n) = id n
eval' (Add e e') = e + e'

eval = cata eval'

-- list type
-- second parameter makes explicit the fact the actual type is a fixed point
-- of some equation L(x) = 1 + L(x)
data L a l = Nil | L a l deriving Show 

type List a = Rec (L a)

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
filtr :: (a -> Bool) -> L a l -> L a l
filtr p Nil     = Nil
filtr p (L a l) | p a = L a l
                | otherwise = Nil

filter' :: (a -> Bool) -> L a (Rec (L a)) -> Rec (L a)
filter' p Nil                 = In Nil
filter' p (L a l) | p a       = In (L a l)
                  | otherwise = l

filter p = cata $ filter' p
~~~~~~~~~

