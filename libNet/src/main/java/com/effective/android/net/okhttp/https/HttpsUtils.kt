package com.effective.android.net.okhttp.https

import android.content.Context
import android.util.Log

import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory

import okhttp3.OkHttpClient

/**
 * https相关处理
 * 如果尝试去访问自签名的网站，则会收到 [javax.net.ssl.SSLHandshakeException]
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
object HttpsUtils {

    private val TAG = HttpsUtils::class.java.simpleName

    fun allowAllCertificates(builder: OkHttpClient.Builder) {
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(TrustAllManager()), SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, TrustAllManager())
            builder.hostnameVerifier(TrustAllHostnameVerifier())
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, e.message)
        } catch (e: KeyManagementException) {
            Log.e(TAG, e.message)
        }

    }

    /**
     * 获取  R.raw.xxxCert 进行加载
     *
     * @param context
     * @param builder
     * @param certificates
     */
    fun allowCertificates(context: Context?, builder: OkHttpClient.Builder, certificates: IntArray?) {
        if (context == null || certificates == null || certificates.size == 0) {
            return
        }
        //CertificateFactory用来证书生成
        val certificateFactory: CertificateFactory
        try {
            certificateFactory = CertificateFactory.getInstance("X.509")
            //Create a KeyStore containing our trusted CAs
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)

            for (i in certificates.indices) {
                //读取本地证书
                val `is` = context.resources.openRawResource(certificates[i])
                keyStore.setCertificateEntry(i.toString(), certificateFactory.generateCertificate(`is`))

                `is`?.close()
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            //Create an SSLContext that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, TrustAllManager())
        } catch (e: KeyStoreException) {
            Log.e(TAG, e.message)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, e.message)
        } catch (e: IOException) {
            Log.e(TAG, e.message)
        } catch (e: KeyManagementException) {
            Log.e(TAG, e.message)
        } catch (e: CertificateException) {
            Log.e(TAG, e.message)
        }

    }
}
