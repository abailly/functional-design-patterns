import org.specs2.mutable._

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
    "apply a pure binary function to its values yielding " in { 
      val v1 = Just(1)
      val v2 = Just(2)
      val plus = Just((x:Int) => (y:Int) => x + y)
      (plus **: v1) **: v2 should be_==(Just(3))
    }

    
  }
}
