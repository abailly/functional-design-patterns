% Patterns of Functional Programming for Simpler Design
% Arnaud Bailly - Cédric Pineau
% 2012-11


## Common Patterns from Literature

### Fowler 

(2 different sources: PEAA et DSL)

#### Fluent Interface

Form of Closure of operations or even DSL,  provides compositionality of actions/functions

#### Expression Builder

Separate building from regular API calls, other examples (more modern): DZone, CumulativeFactory

### Domain Driven Design

##### Immutable Value Object

objects w/o identity, represent characteristics of some Entity or other business object within the system. Immutability favors sharing

##### Side-effect free Functions

self-explanatory!

##### Closure of Operations

provide methods on objects returning same or other object transformed. Some primitive form of monoids and compositionality.

### A Functional Pattern System for OO Design

#### Function Object

first class functions as objects : blocks, closures, lambdas...

#### Value Object

Immutable objects with generator operations

#### Lazy Object

* *Intent*: Defer computation to the latest possible time by encapsulating it in an object that can be computed at any time
* *AKA*: Stream, Pipes & Filters, Pipeline

Sample Code (finite streams):

~~~~~~~~~ {.java .numberLines}
interface Stream<A> {
   A head();
   Stream<A> tail();
   boolean done();
}
~~~~~~~~~

~~~~~~~~~ {.java .numberLines}
abstract class Streams {
  <A> Stream<A> cons(A a, Stream<A> tail) {
}
~~~~~~~~~

#### Transfold

aka. map-reduce

#### Void Value

remove the need for null

#### Translator

replace Visitor as a way to structural recursion

## Common Functional Idioms

### Monoid

provide a base value (eg. Null Object) and some operation for composing objects (similar to  Closure of Operations)

### Option

Signals the possibility of a   non-existing value (eg. Null object)

* Guava's Optional:

~~~~~~~~~ {.java .numberLines}
Optional<String> optional = Optional.fromNullable(aString);
... 
return optional.or(defaultValue); 
~~~~~~~~~

### Zipper

Provide a way to traverse and modify immutable structures

### Iteratee

Decouple enumerating values in a stream of data from computation

### Monad

compose operations while maintaining a context

