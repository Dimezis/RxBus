package rxbus.example

import android.support.v7.app.AppCompatActivity
import rxbus.Bus

abstract class BaseActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        if (Bus.isRegistered(this)) {
            Bus.unregister(this)
        }
    }
}