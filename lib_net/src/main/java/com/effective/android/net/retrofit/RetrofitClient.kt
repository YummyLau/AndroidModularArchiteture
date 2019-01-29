package com.effective.android.net.retrofit

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


    fun <T> getService(tClass: Class<T>): T {
        return getRetrofit(Type.GSON).create(tClass)
    }

    fun <T> getService(@Type type: Int, tClass: Class<T>): T {
        return getRetrofit(type).create(tClass)
    }

    private fun getRetrofit(@Type type: Int): Retrofit {
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
                .baseUrl(BASE_URL)
        return builder.build()
    }

    companion object {

        @Volatile
        private var sClient: RetrofitClient? = null
        private val BASE_URL = "https://c7a894f7-7fd9-4536-935d-5c0d073e7c03.mock.pstmn.io/"

        val instance: RetrofitClient?
            get() {
                if (sClient == null) {
                    synchronized(RetrofitClient::class.java) {
                        if (sClient == null) {
                            sClient = RetrofitClient()
                        }
                    }
                }
                return sClient
            }
    }
}
