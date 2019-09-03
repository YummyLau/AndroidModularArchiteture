package com.effective.android.net.okhttp.https

import android.annotation.SuppressLint

import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import javax.net.ssl.X509TrustManager


/**
 * 一个不验证证书链的证书信任管理器
 * Created by yummyLau on 2018/8/01.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class TrustAllManager : X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        //直接忽略服务端证书，则攻击者可以直接拦截请求然后替换证书
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}
