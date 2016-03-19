package rxbus.bus

import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription

/**
 * Simple Rx Event Bus
 */
object  Bus {
    val bus = SerializedSubject(PublishSubject.create<Any>())

    /**
     * Sends event to Bus. Can be called from any thread
     */
    fun send(event: Any) {
        bus.onNext(event)
    }

    /**
     * Subscribes for events of certain type T.
     */
    inline fun <reified T: Any> observe(): Observable<T> {
        return bus.ofType(T::class.java)
    }
}

/**
 * Extension method to add subscription into CompositeSubscription in more convenient way
 */
fun Subscription.addTo(compositeSubscription: CompositeSubscription) {
    compositeSubscription.add(this)
}