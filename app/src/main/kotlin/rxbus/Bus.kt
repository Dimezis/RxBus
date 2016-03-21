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
     * Avoid using this property directly
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
            subscriptionsMap[subscriber] = null
        }
    }

    /**
     * @return true if subscriber is registered, false otherwise
     */
    fun isRegistered(subscriber: Any): Boolean {
        return subscriptionsMap[subscriber] != null
    }

    /**
     * Registers subscription to correctly unregister it later to avoid memory leaks
     * @param subscription Rx subscription
     * @param subscriber subscriber object that owns this subscription
     * See also more convenient Subscription.registerInBus(subscriber: Any) extension method
     */
    fun register(subscriber: Any, subscription: Subscription) {
        var compositeSubscription = subscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }
        compositeSubscription.add(subscription)
        subscriptionsMap[subscriber] = compositeSubscription
    }
}

/**
 * Used to hold all subscriptions to Bus events and unsubscribe properly when needed.
 * Please always unsubscribe (use unregister method) to avoid memory leaks
 */
private val subscriptionsMap: HashMap<Any, CompositeSubscription?> by lazy {
    HashMap<Any, CompositeSubscription?>()
}

/**
 * Extension method to register subscription in Bus in more convenient way
 */
fun Subscription.registerInBus(subscriber: Any) {
    Bus.register(subscriber, this)
}