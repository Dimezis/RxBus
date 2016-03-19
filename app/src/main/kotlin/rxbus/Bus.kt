package rxbus

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

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