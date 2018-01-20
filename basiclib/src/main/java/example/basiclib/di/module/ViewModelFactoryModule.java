package example.basiclib.di.module;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import example.basiclib.di.ViewModelFactory;

/**
 * 提供各个组件的ViewModelProvider.Factory，避免多个组件同时引用导致编译失败
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/29.
 */

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory factory);
}
