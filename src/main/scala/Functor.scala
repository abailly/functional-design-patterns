
/**
 * A functor F is a transformation that can be applied on objects
 * and on functions alike.
 * 
 * This is a type-class, à la Haskell: We have 2 type parameters, one which is
 * actually a type constructor (F[_], of arity 1) and one which is any type.
 * We use covariance annotation on type parameter A to be able to make it
 * covariant also in (more concrete) subtypes of Functor.
 *
 * The fact that for any A, we can define Functor[A] (eg. functors transform objects)
 * is self-contained in the type definition and need not be made more explicit.
 */
trait Functor[F[+_], +A] { 
  def map[B](f : A => B) : F[B]

  /**
   * In Haskell, map is named fmap and abbreviated <$>. This symbol is
   * not accepted by scala compiler hence the use of double dollars.
   */
  def $$[B](f : A => B) : F[B] = map(f)
}

/**
 * An applicative functor is a functor that has the additional property of allowing
 * "application" of pure functions "within" the functor, thus providing the ability to
 * define sequence of operations "applicatively", eg. like one would do with standard
 * (eg. pure) function application.
 *
 * The prototypical usage is, given a function f : A => B => C and some functor F, to  apply it with arguments
 * pertaining to some functor:
 *   var a : F A
 *   var b : F B
 *   var f' : F f
 *   var c : F C = f' :** a :** b
 *
 */
trait Applicative[F[+_], +A] extends Functor[F,A] {
  def **:[B](f : F[A => B]) : F[B]
}

/**
 * Maybe defines an abstract Functor.
 * Its type parameter is made covariant:
 *
 * A <: B => Maybe[A] <: Maybe[B]
 *
 * This is necessary because otherwise, we could not define an Empty object
 * that would be a valid value for any A in Maybe[A]. This trait is subclassed
 * with a case class for Just, and a case object for Empty.
 */
sealed trait Maybe[+A] extends Applicative[Maybe, A] 

case class Just[A](v : A) extends Maybe[A] {
  def map[B](f : A => B) : Maybe[B] = {
    Just(f(v))
  }

  def **:[B](f : Maybe[A => B]) : Maybe[B] = f match { 
    case Just(ff) => Just(ff(v))
  }
}

case object Empty extends Maybe[Nothing] { 
  def map[B](f : Nothing => B) : Maybe[B] = {
    Empty
  }

  def **:[B](f : Maybe[Nothing => B]) : Maybe[B] = { 
    Empty
  }
}
