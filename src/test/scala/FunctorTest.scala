import org.specs2.mutable._

import curry._

class FunctorAcceptanceSpec extends Specification {
   "as a Functor, a Maybe" should {
     val f = (x:Int) => x + 1
     
     "apply a mapped function on its value when it is not empty" in {
       val v1 = Just(1)
       v1 map f should be_==(Just(2))
     }

     "return empty when mapping a function on empty" in {
       val v1 = Empty
       v1 map f should be_==(Empty)
     }

     "Empty is a valid value of any Maybe" in { 
       val v : Maybe[Int] = Empty
       // we just need to compile, there must be a way to check types
       true should be_==(true)
     }
   } 

  "as an Applicative, a Maybe" should {

    "apply '+' binary function to 2 'Just' values yielding Just the sum of values " in { 
      val v1 = Just(1)
      val v2 = Just(2)
      val plus : Maybe[Int => Int => Int] = Just((x:Int,y:Int) => x + y)
      (plus **: v1) **: v2 should be_==(Just(3))
    }

    "apply '+' binary function to 1 'Just' value and 1 'Empty' yields Empty" in { 
      val plus : Maybe[Int => Int => Int] = Just((x:Int,y:Int) => x + y)
      (plus **: Empty) **: Just(1) should be_==(Empty)
      (plus **: Just(1)) **: Empty should be_==(Empty)
    }

  }

  "as an Applicative, a List" should {

    "apply '+' binary function to lists distributes the sum over all elements" in { 
      import pimps._

      val v1 = List(1,2,3)
      val v2 = List(3,4,5)
      val plus : List[Int => Int => Int] = List((x:Int,y:Int) => x + y)
      (plus **: v1) **: v2 should be_==(List(4,5,6,5,6,7,6,7,8))
    }

  }

}
