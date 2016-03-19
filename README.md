# RxBus

This is a simple Rx Event Bus implementation using Kotlin.
Includes small Android example project.

## How to use
```kotlin
//subscribe to events
Bus.observe<ExampleEvent>()
      .subscribe { doSomething() }
      .addTo(compositeSubscription) //add subscription to your collection to unsubscribe when needed
                
//send events
Bus.send(ExampleEvent(someData))
