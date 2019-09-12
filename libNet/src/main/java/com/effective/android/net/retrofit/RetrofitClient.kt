package com.effective.android.net.retrofit

import android.text.TextUtils
import com.effective.android.net.okhttp.HttpClient
import com.effective.android.net.retrofit.covert.ToByteConvertFactory
import com.effective.android.net.retrofit.covert.ToStringConverterFactory
import com.effective.android.net.retrofit.type.Type
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class RetrofitClient private constructor() {


    fun <T> getService(baseUrl: String? = null, @Type type: Int = Type.GSON, tClass: Class<T>): T {
        return getRetrofit(baseUrl, type).create(tClass)
    }

    private fun getRetrofit(baseUrl: String?, @Type type: Int): Retrofit {
        val builder = Retrofit.Builder()
                .client(HttpClient.instance)
        when (type) {
            Type.GSON -> {
                builder.addConverterFactory(GsonConverterFactory.create())
            }
            Type.BYTE -> {
                builder.addConverterFactory(ToByteConvertFactory.create())
            }
            Type.STRING -> {
                builder.addConverterFactory(ToStringConverterFactory.create())
            }
            else -> {
                builder.addConverterFactory(GsonConverterFactory.create())
            }
        }
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        if (!TextUtils.isEmpty(baseUrl)) {
            builder.baseUrl(baseUrl!!)
        }
        return builder.build()
    }

    companion object {

        @Volatile
        private var sClient: RetrofitClient? = null

        val instance: RetrofitClient
            get() {
                if (sClient == null) {
                    synchronized(RetrofitClient::class.java) {
                        if (sClient == null) {
                            sClient = RetrofitClient()
                        }
                    }
                }
                return sClient!!
            }
    }
}
