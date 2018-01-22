package example.demoaccountservice.di.module;

import javax.inject.Singleton;

import dagger.Module;

/**
 * when you use AccountComponent as lib,this class will be used by yourApp
 * if you have more Module, add in 'includes'
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Singleton
@Module(includes = {
        ActivityBuildersModule.class,
        ViewModelModule.class
})
public class AccountModule {
}
