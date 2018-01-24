package example.weibocomponent.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.weibocomponent.view.MainActivity;

/**
 * @Module 用于标记提供Activity依赖的类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity homeActivity();

}
