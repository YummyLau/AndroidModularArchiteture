package example.weibocomponent.di.component;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import example.basiclib.di.module.ViewModelFactoryModule;
import example.weibocomponent.di.module.DemoModule;

/**
 * Democomponent
 * <p>
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Component(modules = {
        DemoModule.class,
        ViewModelFactoryModule.class,
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class}
        )
public interface DemoComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder context(Application application);

        @BindsInstance
        Builder application(Application application);

        DemoComponent build();
    }

    void inject(Application application);
}
