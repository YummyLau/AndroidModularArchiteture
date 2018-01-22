package example.androidmodulararchiteture.di.module;

import android.arch.lifecycle.ViewModel;


import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import example.androidmodulararchiteture.viewmodel.SplashViewModel;
import example.basiclib.di.ViewModelKey;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/12.
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindsSplashViewModel(SplashViewModel splashViewModel);
}
