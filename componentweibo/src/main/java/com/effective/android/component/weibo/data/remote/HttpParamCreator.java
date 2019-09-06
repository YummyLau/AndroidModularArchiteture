package com.effective.android.component.weibo.data.remote;

import com.effective.android.component.weibo.data.remote.api.WeiboApis;
import com.effective.android.component.weibo.net.HttpParam;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by g8931 on 2017/12/7.
 */

public class HttpParamCreator {

    public static HttpParam create() {
        return new HttpParam.Builder()
                .baseUrl(WeiboApis.BASE_URL)
                .callAdatperFactory(RxJava2CallAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .build();
    }
}
