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
  static <A> Stream<A> cons(A a, Stream<A> tail) {
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

* *Intent*: Signals the possibility of a   non-existing value (eg. Null object)

* Guava's Optional:

~~~~~~~~~ {.java .numberLines}
Optional<String> optional = Optional.fromNullable(aString);
... 
return optional.or(defaultValue); 
~~~~~~~~~

### Lens

* *Intent*: Decouple the operations on element contained in an object from its concrete structure
* *Motivation*:

When Objects are immutable, traversing them when we want to modify/update a value is cumbersome

Example: Update ZIP code of a User
    
~~~~~~~~~ {.java .numberLines}
User user = ...
Address address = user.address;

User updated = user.builder().(
   address.builder().zip("12345").build()
  ).build();
~~~~~~~~~

We would like to have a way to express this update as a composable transformation over Users and addresses. 

Lenses provide this feature as a pair of functions: one for *get*, one for *set*. 

~~~~~~~~~ {.java .numberLines}
User user = ...

Lens<User> userZip = lens("zip", Address.class).in(lens("address", User.class));

User updated = userZip.set(user, "12345");
~~~~~~~~~ 

* *Advantages*:
    * Change logic is manipulable as a first class object, thus decoupling business logic and algorithms from the concrete structure of objecs. One only has to manipulate lenses, compose them, then feed objects at a later stage
    * Depending on implementation, it can be typesafe, checked at compile-time and/or generated automatically from an Object's structure
    * Provides copy-on-write semantics when changing immutable objects
* *Drawbacks*:
    * Awkward to implement in Java, need use of reflection or compile-time magic or bytecode instrumentation to work efficiently
    
### Zipper

* *Intent*: Provide a way to traverse and modify immutable structures
* *AKA*: Derivatives, One-hole Strucuture
* *Relations*:
    * *Lens* pattern: Both let user express and compose changes on an object. But the zipper keeps track of a context thus allowing more complex operations
* *Motivation*:

While modifying complex or nested immutable data structures, one needs to keep track of the context of the traversal and operations on the data to be able to reconstruct a new data which is equivalent to the old one but for some changes. This bookkeeping is tedious, cumbersome and error-prone. 

Example: update all postings related to a user using some conversion function

~~~~~~~~~ {.java .numberLines}
User user = ...
Function updatePostings = ...

User newUser = UserZipper.zip(user).postings().update(updatePostings).build();
~~~~~~~~~

* *Advantages*: 
    * Remove bookkeeping burden from the developer while retaining the ability to point to some part of structure 
    * Simplify updates of immutable structure
    * Decouple transformations of values within structures from the structure holding them
* *Drawbacks*: 
    * Each type need a specific Zipper construct as its navigation is specific
    
### Iteratee

* *Intent*: Decouple enumerating values in a stream of data from computation

### Applicative


### Monad

* *Intent*: provide a context for composing operations

### Co-monad

* *Intent*: ???


