package com.effective.android.component.weibo;

import android.app.Application;

import com.effective.android.component.weibo.data.DemoRepository;
import com.effective.android.component.weibo.data.local.db.AppDataBase;
import com.effective.android.component.weibo.data.remote.api.WeiboApis;
import com.effective.android.component.weibo.net.HttpClient;
import com.effective.android.component.weibo.net.HttpParam;
import com.effective.android.service.account.AccountSdk;
import com.plugin.component.IComponent;
import com.plugin.component.SdkManager;
import com.plugin.component.anno.AutoInjectComponent;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@AutoInjectComponent(impl = WeiboSdkImpl.class)
public class WeiboComponent implements IComponent {

    public static DemoRepository demoRepository;
    public static AccountSdk accountSdk;

    @Override
    public void attachComponent(Application application) {
        HttpParam httpParam = new HttpParam.Builder()
                .baseUrl(WeiboApis.BASE_URL)
                .callAdatperFactory(RxJava2CallAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .build();
        demoRepository = new DemoRepository(AppDataBase.getInstance(application), HttpClient.create(httpParam, WeiboApis.class));
        accountSdk = SdkManager.getSdk(AccountSdk.class);
    }

    @Override
    public void detachComponent() {
        demoRepository = null;
        accountSdk = null;
    }
}
