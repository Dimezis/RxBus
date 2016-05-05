package rxbus.example

import android.os.Bundle
import eightbitlab.com.rxbus.R
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rxbus.Bus
import rxbus.Bus.registerInBus

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeToExampleEvents()
        setClickListeners()
    }

    private fun setClickListeners() {
        //produce example events on click
        buttonEvent1.setOnClickListener {
            Bus.send(Events.ExampleEvent1("ExampleEvent1 Text"))
        }

        var counter = 0
        buttonEvent2.setOnClickListener {
            counter++
            Bus.send(Events.ExampleEvent2(count = counter))
        }
    }

    private fun subscribeToExampleEvents() {
        Bus.observe<Events.ExampleEvent1>()
                .map { "Modified ${it.title}"} //you can use any Rx operator to transform event data
                .subscribe { textView.text = it }
                .registerInBus(this)

        Bus.observe<Events.ExampleEvent2>()
                .observeOn(AndroidSchedulers.mainThread()) //optional, if you need to receive event in main thread
                .subscribe { textView.text = "ExampleEvent2 sent ${it.count} times" }
                .registerInBus(this)

        //if you want to manage each of your subscriptions separately, you can store them locally
        var notRegisteredSubscription = Bus.observe<Events.ExampleEvent1>()
                .subscribe { textView.text = it.title }
                //here we don't call registerInBus(this)

        //... somewhere later, unsubscribe when you need
        notRegisteredSubscription.unsubscribe()
    }
}
