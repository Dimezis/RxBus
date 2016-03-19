package rxbus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import rx.subscriptions.CompositeSubscription

abstract class BaseActivity : AppCompatActivity() {
    /**
     * Used to hold all subscriptions to Bus events and unsubscribe properly when activity is destroyed.
     * Please always unsubscribe to avoid memory leaks
     */
    val compositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }
}