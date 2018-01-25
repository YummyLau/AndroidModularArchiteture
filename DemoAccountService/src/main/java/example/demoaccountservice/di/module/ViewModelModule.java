package example.demoaccountservice.di.module;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import example.basiclib.di.ViewModelKey;
import example.demoaccountservice.viewmodel.LoginViewModel;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindsLoginViewModel(LoginViewModel loginViewModel);
}
