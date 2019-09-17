package com.effective.android.component.weibo.net.interceptor;


import com.effective.android.component.weibo.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Cache interceptor
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class CacheInterceptor implements Interceptor {

    private boolean isCache;
    private boolean isNetAvailable;

    public CacheInterceptor(boolean isCache, boolean isNetAvailable) {
        this.isCache = isCache;
        this.isNetAvailable = isNetAvailable;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oriRequest = chain.request();
        Request.Builder requestBuilder = oriRequest.newBuilder();

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);


        CacheControl.Builder cacheControlBuilder = new CacheControl.Builder();
        if (isNetAvailable) {
            if (isCache) {
                cacheControlBuilder = cacheControlBuilder
                        .maxAge(Constants.MAX_AGE, TimeUnit.SECONDS)
                        .maxStale(0, TimeUnit.SECONDS);
            } else {
                cacheControlBuilder = cacheControlBuilder.noCache();
            }
        } else {
            cacheControlBuilder = cacheControlBuilder
                    .maxAge(Constants.MAX_AGE, TimeUnit.SECONDS)
                    .maxStale(Constants.MAX_STALE, TimeUnit.SECONDS);
        }

        CacheControl cacheControl = cacheControlBuilder.build();
        requestBuilder.cacheControl(cacheControl)
                .method(oriRequest.method(), oriRequest.body());


        return response;
    }
}