package example.componentweibo;


import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import example.basiclib.net.HttpClient;
import example.basiclib.net.HttpParam;
import example.weibocomponent.data.DemoRepository;
import example.weibocomponent.data.local.db.AppDataBase;
import example.weibocomponent.data.remote.api.WeiboApis;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class App extends Application {

    public static DemoRepository demoRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        HttpParam httpParam = new HttpParam.Builder()
                .baseUrl(WeiboApis.BASE_URL)
                .callAdatperFactory(RxJava2CallAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .build();
        demoRepository = new DemoRepository(AppDataBase.getInstance(this),HttpClient.create(httpParam, WeiboApis.class));
    }
}
