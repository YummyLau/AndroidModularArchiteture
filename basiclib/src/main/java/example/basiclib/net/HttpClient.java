package example.basiclib.net;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import example.basiclib.net.interceptor.CacheInterceptor;
import example.basiclib.net.interceptor.LogInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * http client
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public class HttpClient {

    private static OkHttpClient.Builder sHttpClientBuilder;
    private static Context sContext;

    public static void init(Application application) {
        sContext = application;
        sHttpClientBuilder = new OkHttpClient.Builder();
    }

    public static <T> T create(@NonNull HttpParam httpParam, @NonNull Class<T> apiClass) {
        if (sContext == null) {
            throw new RuntimeException("you should init in Application#onCreate!");
        }
        if (httpParam == null || apiClass == null) {
            new IllegalArgumentException("illegalArgument!");
        }
        sHttpClientBuilder.connectTimeout(httpParam.connectTimeOut, TimeUnit.SECONDS);
        sHttpClientBuilder.readTimeout(httpParam.readTimeOut, TimeUnit.SECONDS);
        sHttpClientBuilder.writeTimeout(httpParam.writeTimeOut, TimeUnit.SECONDS);

        sHttpClientBuilder.interceptors().clear();
        sHttpClientBuilder.addInterceptor(new CacheInterceptor(httpParam.cache, NetStatusChecker.networkAvailable(sContext)));
        if (httpParam.debug) {
            sHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
            sHttpClientBuilder.addInterceptor(new LogInterceptor());
        }

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(httpParam.baseUrl)
                .addConverterFactory(httpParam.converterFactory)
                .addCallAdapterFactory(httpParam.callAdapterFactory);

        return builder.client(sHttpClientBuilder.build())
                .build().create(apiClass);

    }

}
