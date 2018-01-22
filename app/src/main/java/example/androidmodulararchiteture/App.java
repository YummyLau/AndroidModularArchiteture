package example.androidmodulararchiteture;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import example.androidmodulararchiteture.di.component.DaggerDagger2Component;
import example.basiclib.net.HttpClient;
import example.basiclib.support.CrashManager;
import example.componentlib.component.ComponentManager;
import example.componentlib.component.interfaces.IDemoComponent;
import example.componentlib.service.ServiceManager;
import example.demoaccountservice.AccountServiceImpl;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class App extends Application implements HasActivityInjector {


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //dagger2
        DaggerDagger2Component.builder()
                .context(this)
                .application(this)
                .build().inject(this);


        //crash
        CrashManager.getInstance().init(this);

        ARouter.init(this);
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
            ARouter.printStackTrace();
            Stetho.initializeWithDefaults(this);
        }

        HttpClient.init(this);

        //初始化基础服务
        ServiceManager.register(this, AccountServiceImpl.class);
        //初始化组件
        ComponentManager.bind(this, IDemoComponent.class);
    }

}
