package rxbus.example

import android.support.v7.app.AppCompatActivity
import com.eightbitlab.rxbus.Bus

abstract class BaseActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }
}