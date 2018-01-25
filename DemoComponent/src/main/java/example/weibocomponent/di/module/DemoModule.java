package example.weibocomponent.di.module;

import javax.inject.Singleton;

import dagger.Module;

/**
 * Demo 模块所有modules
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Singleton
@Module(includes = {
        AppModule.class,
        ActivityBuildersModule.class,
        ViewModelModule.class})
public class DemoModule {
}
