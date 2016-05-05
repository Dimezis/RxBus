package rxbus

import android.util.Log
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Simple Rx Event Bus
 */
object Bus {
    private val TAG = javaClass.simpleName

    /**
     * Used to hold all subscriptions to Bus events and unsubscribe properly when needed.
     * Please always unsubscribe (use unregister method) to avoid memory leaks
     */
    private val subscriptionsMap: HashMap<Any, CompositeSubscription?> by lazy {
        HashMap<Any, CompositeSubscription?>()
    }

    /**
     * Avoid using this property directly, exposed only because it's used in inline fun
     */
    val bus = SerializedSubject(PublishSubject.create<Any>())

    /**
     * Sends an event to Bus. Can be called from any thread
     */
    fun send(event: Any) {
        bus.onNext(event)
    }

    /**
     * Subscribes for events of certain type T. Can be called from any thread
     */
    inline fun <reified T: Any> observe(): Observable<T> {
        return bus.ofType(T::class.java)
    }

    /**
     * Unregisters subscriber from Bus events.
     * Calls unsubscribe method of your subscriptions
     * @param subscriber subscriber to unregister
     * @throws RuntimeException if you didn't registered this subscriber
     */
    fun unregister(subscriber: Any) {
        var compositeSubscription = subscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            Log.w(TAG, "Trying to unregister subscriber that wasn't registered")
        } else {
            compositeSubscription.clear()
            subscriptionsMap.remove(subscriber)
        }
    }


    private fun register(subscriber: Any, subscription: Subscription) {
        var compositeSubscription = subscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }
        compositeSubscription.add(subscription)
        subscriptionsMap[subscriber] = compositeSubscription
    }

    /**
     * Registers the subscription to correctly unregister it later to avoid memory leaks
     * @param subscriber subscriber object that owns this subscription
     */
    fun Subscription.registerInBus(subscriber: Any) {
        Bus.register(subscriber, this)
    }
}
