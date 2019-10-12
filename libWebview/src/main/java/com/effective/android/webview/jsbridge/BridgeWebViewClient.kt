package com.effective.android.webview.jsbridge

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.effective.android.webview.interfaces.OnFinishCallback

import com.tencent.smtt.export.external.interfaces.ClientCertRequest
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * 如果要自定义WebViewClient必须要集成此类
 * Created by bruce on 10/28/15.
 */
class BridgeWebViewClient(private var webView: BridgeWebViewKotlin) : WebViewClient() {

    private var loadStart: Boolean = false
    private var loadError: Boolean = false
    private var currentUrl: String? = null
    private var mOnFinishCallback: OnFinishCallback? = null

    @JvmField
    var proxy: WebViewClient? = null

    fun setProxy(proxy: WebViewClient) {
        this.proxy = proxy
    }

    fun setOnFinishCallback(mOnFinishCallback: OnFinishCallback) {
        this.mOnFinishCallback = mOnFinishCallback
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.d(TAG, "shouldOverrideUrlLoading")
        loadStart = false
        currentUrl = url
        try {
            currentUrl = URLDecoder.decode(currentUrl, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        if (currentUrl!!.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(currentUrl!!)
            return true
        } else if (currentUrl!!.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue()
            return true
        } else {
            return if (proxy != null) {
                proxy!!.shouldOverrideUrlLoading(view, url)
            } else {
                super.shouldOverrideUrlLoading(view, currentUrl)
            }
        }
    }


    // 增加shouldOverrideUrlLoading在api》=24时
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        Log.d(TAG, "shouldOverrideUrlLoading api24")
        loadStart = false
        currentUrl = request.url.toString()
        //锤子手机 api 23 就回调了=。=
        try {
            currentUrl = URLDecoder.decode(currentUrl, "UTF-8")
        } catch (ex: UnsupportedEncodingException) {
            ex.printStackTrace()
        }

        if (currentUrl!!.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(currentUrl!!)
            return true
        } else if (currentUrl!!.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue()
            return true
        } else {
            return if (proxy != null) {
                proxy!!.shouldOverrideUrlLoading(view, request)
            } else {
                super.shouldOverrideUrlLoading(view, request)
            }
        }
    }


    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (loadStart) {
            if (loadError) {
                loadError = false
                if (mOnFinishCallback != null && !TextUtils.isEmpty(url)) {
                    mOnFinishCallback!!.onFail(url!!)
                }
            } else {
                loadError = false
                if (mOnFinishCallback != null && !TextUtils.isEmpty(url)) {
                    mOnFinishCallback!!.onSuccess(url!!)
                }
            }
            loadStart = false
            if (!webView.settings.loadsImagesAutomatically) {
                webView.settings.loadsImagesAutomatically = true
            }
        }

        if (BridgeWebViewKotlin.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view!!, BridgeWebViewKotlin.toLoadJs)
        }
        if (webView.startupMessage != null) {
            for (m in webView.startupMessage!!) {
                webView.dispatchMessage(m)
            }
            webView.startupMessage = null
        }

        if (proxy != null) {
            proxy!!.onPageFinished(view, url)
        }
    }

    /**
     * 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
     */
    override fun onLoadResource(webView: WebView?, s: String?) {
        Log.d(TAG, "onLoadResource")
        if (proxy != null) {
            proxy!!.onLoadResource(webView, s)
        } else {
            super.onLoadResource(webView, s)
        }
    }

    override fun onPageStarted(webView: WebView, s: String, bitmap: Bitmap?) {
        Log.d(TAG, "onPageStarted")
        loadStart = true
        currentUrl = s
        if (proxy != null) {
            proxy!!.onPageStarted(webView, s, bitmap)
        } else {
            super.onPageStarted(webView, s, bitmap)
        }
    }

    override fun onReceivedError(webView: WebView?, i: Int, s: String?, s1: String?) {
        Log.d(TAG, "onReceivedError")
        loadError = true
        if (proxy != null) {
            proxy!!.onReceivedError(webView, i, s, s1)
        } else {
            super.onReceivedError(webView, i, s, s1)
        }
    }

    override fun onReceivedError(webView: WebView, webResourceRequest: WebResourceRequest, webResourceError: WebResourceError) {
        Log.d(TAG, "onReceivedError")
        if (proxy != null) {
            proxy!!.onReceivedError(webView, webResourceRequest, webResourceError)
        } else {
            super.onReceivedError(webView, webResourceRequest, webResourceError)
        }
    }

    override fun onReceivedHttpError(webView: WebView?, webResourceRequest: WebResourceRequest?, webResourceResponse: WebResourceResponse?) {
        Log.d(TAG, "onReceivedHttpError API 23")
        if (proxy != null) {
            proxy!!.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
        } else {
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
        }
    }

    /**
     * 在每一次请求资源时，都会通过这个函数来回调，在非UI线程中执行的
     */
    override fun shouldInterceptRequest(webView: WebView?, s: String?): WebResourceResponse? {
        Log.d(TAG, "shouldInterceptRequest(webView: WebView?, s: String?)")
        return if (proxy != null) {
            proxy!!.shouldInterceptRequest(webView, s)
        } else {
            super.shouldInterceptRequest(webView, s)
        }
    }

    override fun shouldInterceptRequest(webView: WebView, webResourceRequest: WebResourceRequest): WebResourceResponse? {
        Log.d(TAG, "shouldInterceptRequest(webView: WebView, webResourceRequest: WebResourceRequest)")
        return if (proxy != null) {
            proxy!!.shouldInterceptRequest(webView, webResourceRequest)
        } else {
            super.shouldInterceptRequest(webView, webResourceRequest)
        }
    }

    override fun shouldInterceptRequest(webView: WebView, webResourceRequest: WebResourceRequest, bundle: Bundle?): WebResourceResponse? {
        Log.d(TAG, "shouldInterceptRequest(webView: WebView, webResourceRequest: WebResourceRequest, bundle: Bundle?)")
        return if (proxy != null) {
            proxy!!.shouldInterceptRequest(webView, webResourceRequest, bundle)
        } else {
            super.shouldInterceptRequest(webView, webResourceRequest, bundle)
        }
    }


    /**
     * 更新访问历史
     */
    override fun doUpdateVisitedHistory(webView: WebView?, s: String?, b: Boolean) {
        Log.d(TAG, "doUpdateVisitedHistory")
        if (proxy != null) {
            proxy!!.doUpdateVisitedHistory(webView, s, b)
        } else {
            super.doUpdateVisitedHistory(webView, s, b)
        }
    }

    /**
     * 是否重发POST请求数据，默认不重发
     */
    override fun onFormResubmission(webView: WebView?, message: android.os.Message, message1: android.os.Message?) {
        Log.d(TAG, "onFormResubmission")
        if (proxy != null) {
            proxy!!.onFormResubmission(webView, message, message1)
        } else {
            super.onFormResubmission(webView, message, message1)
        }
    }

    /**
     * 通知主程序：WebView接收HTTP认证请求，主程序可以使用HttpAuthHandler为请求设置WebView响应。默认取消请求
     */
    override fun onReceivedHttpAuthRequest(webView: WebView?, httpAuthHandler: HttpAuthHandler, s: String?, s1: String?) {
        Log.d(TAG, "onReceivedHttpAuthRequest")
        if (proxy != null) {
            proxy!!.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1)
        } else {
            super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1)
        }
    }

    /**
     * 接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler, sslError: SslError?) {
        Log.d(TAG, "onReceivedSslError")
        if (proxy != null) {
            proxy!!.onReceivedSslError(webView, sslErrorHandler, sslError)
        } else {
            sslErrorHandler.proceed()
            //            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    /**
     * 通知主程序处理SSL客户端认证请求。如果需要提供密钥，主程序负责显示UI界面。
     * 有三个响应方法：proceed(), cancel() 和 ignore()。
     * 如果调用proceed()和cancel()，webview将会记住response，
     * 对相同的host和port地址不再调用onReceivedClientCertRequest方法。
     * 如果调用ignore()方法，webview则不会记住response。该方法在UI线程中执行，
     * 在回调期间，连接被挂起。默认cancel()，即无客户端认证
     */
    @RequiresApi(21)
    override fun onReceivedClientCertRequest(webView: WebView?, clientCertRequest: ClientCertRequest) {
        Log.d(TAG, "onReceivedClientCertRequest")
        if (proxy != null) {
            proxy!!.onReceivedClientCertRequest(webView, clientCertRequest)
        } else {
            super.onReceivedClientCertRequest(webView, clientCertRequest)
        }
    }

    /**
     * WebView发生改变时调用
     */
    override fun onScaleChanged(webView: WebView?, v: Float, v1: Float) {
        Log.d(TAG, "onScaleChanged")
        if (proxy != null) {
            proxy!!.onScaleChanged(webView, v, v1)
        } else {
            super.onScaleChanged(webView, v, v1)
        }
    }

    /**
     * 通知主程序输入事件不是由WebView调用。是否让主程序处理WebView未处理的Input Event。
     * 除了系统按键，WebView总是消耗掉输入事件或shouldOverrideKeyEvent返回true。
     * 该方法由event 分发异步调用。注意：如果事件为MotionEvent，则事件的生命周期只存在方法调用过程中，
     * 如果WebViewClient想要使用这个Event，则需要复制Event对象。
     */
    override fun onUnhandledKeyEvent(webView: WebView?, keyEvent: KeyEvent?) {
        Log.d(TAG, "onUnhandledKeyEvent")
        if (proxy != null) {
            proxy!!.onUnhandledKeyEvent(webView, keyEvent)
        } else {
            super.onUnhandledKeyEvent(webView, keyEvent)
        }
    }

    /**
     * 重写此方法才能够处理在浏览器中的按键事件。
     * 是否让主程序同步处理Key Event事件，如过滤菜单快捷键的Key Event事件。
     * 如果返回true，WebView不会处理Key Event，
     * 如果返回false，Key Event总是由WebView处理。默认：false
     *
     * @return
     */
    override fun shouldOverrideKeyEvent(webView: WebView?, keyEvent: KeyEvent?): Boolean {
        Log.d(TAG, "shouldOverrideKeyEvent")
        return if (proxy != null) {
            proxy!!.shouldOverrideKeyEvent(webView, keyEvent)
        } else {
            super.shouldOverrideKeyEvent(webView, keyEvent)
        }
    }

    override fun onTooManyRedirects(webView: WebView?, message: android.os.Message?, message1: android.os.Message?) {
        Log.d(TAG, "onTooManyRedirects")
        if (proxy != null) {
            proxy!!.onTooManyRedirects(webView, message, message1)
        } else {
            super.onTooManyRedirects(webView, message, message1)
        }
    }

    /**
     * 通知主程序执行了自动登录请求
     */
    override fun onReceivedLoginRequest(webView: WebView?, s: String?, s1: String?, s2: String?) {
        Log.d(TAG, "onReceivedLoginRequest")
        if (proxy != null) {
            proxy!!.onReceivedLoginRequest(webView, s, s1, s2)
        } else {
            super.onReceivedLoginRequest(webView, s, s1, s2)
        }
    }

    override fun onDetectedBlankScreen(s: String?, i: Int) {
        if (proxy != null) {
            proxy!!.onDetectedBlankScreen(s, i)
        } else {
            super.onDetectedBlankScreen(s, i)
        }
    }

    companion object {
        private val TAG = BridgeWebViewClient::class.java.simpleName
    }
}