package example.weibocomponent.di.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.basiclib.net.HttpClient;
import example.basiclib.net.HttpParam;
import example.weibocomponent.data.DemoRepository;
import example.weibocomponent.data.local.db.AppDataBase;
import example.weibocomponent.data.local.db.dao.StatusDao;
import example.weibocomponent.data.local.db.dao.UserDao;
import example.weibocomponent.data.remote.api.WeiboApis;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Provides 标记Module中返回依赖的方法
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    DemoRepository provideFeatureRepository(AppDataBase appDataBase, WeiboApis weiboApis) {
        return new DemoRepository(appDataBase, weiboApis);
    }

    @Provides
    @Singleton
    AppDataBase provideDB(Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DB_FILE_NAME).build();
    }


    @Singleton
    @Provides
    WeiboApis provideWeiboService() {
        HttpParam httpParam = new HttpParam.Builder()
                .baseUrl(WeiboApis.BASE_URL)
                .callAdatperFactory(RxJava2CallAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .build();
        return HttpClient.create(httpParam, WeiboApis.class);
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDataBase db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    StatusDao provideStatusDao(AppDataBase db) {
        return db.statusDao();
    }


}
