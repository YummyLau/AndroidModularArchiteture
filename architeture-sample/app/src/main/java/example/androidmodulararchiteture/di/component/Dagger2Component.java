package example.androidmodulararchiteture.di.component;

import android.app.Application;
import android.content.Context;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import example.androidmodulararchiteture.App;
import example.androidmodulararchiteture.di.module.ActivityBuildersModule;
import example.androidmodulararchiteture.di.module.ViewModelModule;
import example.basiclib.di.module.ViewModelFactoryModule;
import example.demoaccountservice.di.module.AccountModule;
import example.weibocomponent.di.module.DemoModule;

/**
 * app dagger2 dependence inject!
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

@Singleton
@Component(
        modules = {
                //basic
                ViewModelFactoryModule.class,
                AndroidSupportInjectionModule.class,

                //activity and viewmodule
                ActivityBuildersModule.class,
                ViewModelModule.class,

                //account module
                AccountModule.class,
                DemoModule.class
        })
public interface Dagger2Component {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder context(Context context);

        @BindsInstance
        Builder application(Application application);

        Dagger2Component build();
    }

    void inject(App application);
}
