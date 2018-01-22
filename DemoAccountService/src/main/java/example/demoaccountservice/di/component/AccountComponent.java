package example.demoaccountservice.di.component;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import example.basiclib.di.module.ViewModelFactoryModule;
import example.demoaccountservice.di.module.AccountModule;


/**
 * when you use AccountComponent as App,this class will be used by dagger2
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

@Component(modules = {AccountModule.class,
        ViewModelFactoryModule.class,
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class})
public interface AccountComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder context(Application application);

        @BindsInstance
        Builder application(Application application);

        AccountComponent build();
    }

    void inject(Application application);
}
