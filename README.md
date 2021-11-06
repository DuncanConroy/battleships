# battleships

A simulation of the famous battleship board game


## A quick intro to the Kotlin syntax
###Function/Method syntax
```
fun hello(param1: String): String
 ^   ^     ^       ^        ^- return type
 |   |     |       |- parameter type
 |   |     |- parameter name
 |   |- function name
 |- function keyword
```

###Variables
Kotlin is always strictly typed, but it's not necessary to add the type declaration to every element.
```
var foo = "bar" // a mutable variable of type String (the type is automatically inferred by the compiler)
var foo: String = "bar" // same as above, but ": String" is obsolete

val foo = "bar" // an immutable variable of type String (the type is automatically inferred by the compiler)
foo = "hello" // not possible, as immutable variables cannot be changed after initialization

var foo: String? = null // by default, variables in Kotlin cannot be null, unless suffixed with ?
```

###Control flows
Kotlin makes use of functional and oo principles.
```
val list = listOf("A","B","C")
val c = list.find { it == "C" } // c is of type :String?, as find could return null

var ships: List<Ship?> = listOf(null, Ship("Curiser"))
println(ships[0]?.name) // prints null, due to the ? safecall
println(ships[0]!!.name) // throws an NPE, due to !! not-null assertion operator
println(ships[0].name) // does not compile, because list elements can be nullable
println(ships[1].name) // prints "Cruiser"

var ship: Ship?
[..] // more code
ship?.let { // same logic as with "if ship != null"
  // do something useful
}
```