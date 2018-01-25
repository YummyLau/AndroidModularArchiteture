package example.androidmodulararchiteture.di.module;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.androidmodulararchiteture.view.SplashActivity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();
}
