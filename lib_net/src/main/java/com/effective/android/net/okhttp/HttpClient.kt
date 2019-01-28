package com.effective.android.net.okhttp


import android.app.Application

import com.effective.android.base.core.net.okhttp.cookie.OkHttpCookieJar
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 *
 *
 * okhttp3采用构造者模式来实现的
 *
 *
 * Request.Builder 请求构造者
 * url(String url)：请求的url
 * post()：默认是Get方式
 * post(RequestBody body)：Post带参数
 * build()：构造请求
 *
 *
 * 请求参数有三种：
 * RequestBody：普通的请求参数
 * FormBody.Builder：以表单的方式传递键值对的请求参数
 * MultipartBody.Builder：以表单的方式上传文件的请求参数
 *
 *
 * 执行方法：
 * Call
 * enqueue(Callback callback)：异步请求
 * execute()：同步请求
 */
object HttpClient {

    private val TAG = HttpClient::class.java.simpleName

    private var sApplication: Application? = null
    private var sDebug: Boolean = false
    @Volatile
    private var sClient: OkHttpClient? = null

    private val CONNECT_TIMEOUT = 10
    private val READ_TIMEOUT = 30
    private val WRITE_TIMEOUT = 30

    /**
     * 上传下载需要注意下读写设置
     *
     * @return
     */
    //log
    //stetho
    //证书
    //HttpsUtils.allowAllCertificates(builder);
    //cookie
    val instance: OkHttpClient?
        get() {
            if (sApplication == null) {
                throw RuntimeException("$TAG#getInstancesApplication can't be null ! please invoke init at Application#onCreate")
            }
            if (sClient == null) {
                synchronized(HttpClient::class.java) {
                    if (sClient == null) {
                        val builder = OkHttpClient.Builder()

                        if (sDebug) {
                            val logInterceptor = HttpLoggingInterceptor()
                            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                            builder.addInterceptor(logInterceptor)
                            builder.addNetworkInterceptor(StethoInterceptor())
                        }
                        builder.cookieJar(OkHttpCookieJar(sApplication))

                        sClient = builder.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .build()
                    }
                }
            }
            return sClient
        }

    /**
     * 获取全局application
     */
    fun init(application: Application, debug: Boolean) {
        sApplication = application
        sDebug = debug
        if (sDebug) {
            Stetho.initializeWithDefaults(application)
        }
    }

}
