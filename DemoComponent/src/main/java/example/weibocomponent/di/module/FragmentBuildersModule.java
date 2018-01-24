package example.weibocomponent.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.weibocomponent.view.HomeFragment;

/**
 * fragment依赖管理
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Module
public abstract class FragmentBuildersModule {


    @ContributesAndroidInjector
    abstract HomeFragment contributeFollowedFragment();
}
