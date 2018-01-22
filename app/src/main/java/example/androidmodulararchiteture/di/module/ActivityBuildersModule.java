package example.androidmodulararchiteture.di.module;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.androidmodulararchiteture.view.SplashActivity;

/**
 * Created by g8931 on 2017/12/12.
 */
@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();
}
