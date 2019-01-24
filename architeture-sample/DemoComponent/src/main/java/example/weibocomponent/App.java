package example.weibocomponent;


import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import example.weibocomponent.data.local.db.AppDataBase;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
    }
}
