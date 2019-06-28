package example.androidmodulararchiteture;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class App extends Application{


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

}
