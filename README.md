# RxBus

This is a simple Rx Event Bus implementation using Kotlin.
Includes small Android example project.

## How to use
```kotlin
//subscribe to events
Bus.observe<ExampleEvent>()
      .subscribe { doSomething() }
      .registerInBus(this) //registers your subscription to unsubscribe it properly
                
//send events
Bus.send(ExampleEvent(someData))

//unsubscribe from events
if (Bus.isRegistered(this)) {
    Bus.unregister(this)
}
