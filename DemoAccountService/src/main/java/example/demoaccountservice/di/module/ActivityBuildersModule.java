package example.demoaccountservice.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.demoaccountservice.view.LoginActivity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract LoginActivity homeActivity();

}
