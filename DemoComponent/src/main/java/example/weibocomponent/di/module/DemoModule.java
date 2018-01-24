package example.weibocomponent.di.module;

import javax.inject.Singleton;

import dagger.Module;

/**
 * feature 模块所有modules
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Singleton
@Module(includes = {
        AppModule.class,
        ActivityBuildersModule.class,
        ViewModelModule.class})
public class DemoModule {
}
