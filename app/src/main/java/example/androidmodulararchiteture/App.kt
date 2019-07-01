package example.androidmodulararchiteture
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
