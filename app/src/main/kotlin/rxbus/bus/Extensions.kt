package rxbus.bus

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Extension method to add subscription into CompositeSubscription in more convenient way
 */
fun Subscription.addTo(compositeSubscription: CompositeSubscription) {
    compositeSubscription.add(this)
}
