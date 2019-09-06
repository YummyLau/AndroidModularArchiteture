package com.effective.android.component.weibo;

import android.app.Application;

import com.effective.android.component.weibo.data.local.db.AppDataBase;
import com.effective.android.component.weibo.data.remote.api.WeiboApis;
import com.plugin.component.IComponent;
import com.plugin.component.anno.AutoInjectComponent;

@AutoInjectComponent(impl = WeiboSdkImpl.class)
public class WeiboComponent implements IComponent {

    public static DemoRepository demoRepository;

    @Override
    public void attachComponent(Application application) {
        HttpParam httpParam = new HttpParam.Builder()
                .baseUrl(WeiboApis.BASE_URL)
                .callAdatperFactory(RxJava2CallAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .build();
        demoRepository = new DemoRepository(AppDataBase.getInstance(this),HttpClient.create(httpParam, WeiboApis.class));
    }

    @Override
    public void detachComponent() {

    }
}
