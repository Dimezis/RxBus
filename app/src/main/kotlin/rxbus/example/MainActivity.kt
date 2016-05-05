package rxbus.example

import android.os.Bundle
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.Bus.registerInBus
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeToExampleEvents()
        setClickListeners()
    }

    private fun setClickListeners() {
        //produce example events on click
        buttonTextEvent.setOnClickListener {
            Bus.send(Events.TextEvent("Example Text"))
        }

        var counter = 0
        buttonCounterEvent.setOnClickListener {
            counter++
            Bus.send(Events.CounterEvent(count = counter))
        }
    }

    private fun subscribeToExampleEvents() {
        Bus.observe<Events.TextEvent>()
                .map { "Modified ${it.title}"} //you can use any Rx operator to transform event data
                .subscribe { textView.text = it }
                .registerInBus(this)

        Bus.observe<Events.CounterEvent>()
                .observeOn(AndroidSchedulers.mainThread()) //optional, if you need to receive event in main thread
                .subscribe { textView.text = "Counter event sent ${it.count} times" }
                .registerInBus(this)

        //if you want to manage each of your subscriptions separately, you can store them locally
        var notRegisteredSubscription = Bus.observe<Events.TextEvent>()
                .subscribe { textView.text = it.title }
                //here we don't call registerInBus(this)

        //... somewhere later, unsubscribe when you need
        notRegisteredSubscription.unsubscribe()
    }
}
