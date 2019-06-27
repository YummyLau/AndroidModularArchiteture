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

        //crash
//        CrashManager.getInstance().init(this);
//
//        ARouter.init(this);
//        if (BuildConfig.DEBUG) {
//            ARouter.openDebug();
//            ARouter.openLog();
//            ARouter.printStackTrace();
//            Stetho.initializeWithDefaults(this);
//        }
//
//        HttpClient.init(this);
//
//        //初始化基础服务
//        ServiceManager.register(this, AccountServiceImpl.class);
//        ServiceManager.register(this, SkinServiceImpl.class);

//        //初始化组件
//        ComponentManager.bind(this, DemoComponentImpl.class);
    }

}
