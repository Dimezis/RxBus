package rxbus

import android.os.Bundle
import eightbitlab.com.rxbus.R
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
                .subscribe { textView.text = it.title }
                .addTo(compositeSubscription)

        Bus.observe<Events.ExampleEvent2>()
                .observeOn(AndroidSchedulers.mainThread()) //optional, if you need to receive event in main thread
                .subscribe { textView.text = "ExampleEvent2 sent ${it.count} times" }
                .addTo(compositeSubscription)
    }
}
