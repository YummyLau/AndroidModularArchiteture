package com.effective.android.webview

import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View

import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.export.external.interfaces.JsPromptResult
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

/**
 * 文件上传
 * Created by yummylau on 2018/5/7.
 */
open class X5WebChromeClient : WebChromeClient {

    constructor() : this(null)

    constructor(webChromeClient: WebChromeClient?) {
        this.webChromeClient = webChromeClient
    }

    private var webChromeClient: WebChromeClient? = null


    //<input type="file"> 回调
    // For Android < 3.0
    open fun openFileChooser(valueCallback: ValueCallback<Uri>) {
        Log.d(TAG, "openFileChooser < 3.0")
    }

    // For Android  >= 3.0
    open fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String) {
        Log.d(TAG, "openFileChooser >= 3.0")
    }

    //For Android  >= 4.1
    override fun openFileChooser(valueCallback: ValueCallback<Uri>,
                                 acceptType: String?, capture: String?) {
        Log.d(TAG, "openFileChooser >= 4.1")
    }

    // For Android >= 5.0
    @RequiresApi(21)
    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?): Boolean {
        Log.d(TAG, "onShowFileChooser >= 5.0")
        return webChromeClient?.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                ?: super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    /**
     * 通知程序当前页面加载进度
     *
     * @param view
     * @param newProgress
     */
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        Log.d(TAG, "onProgressChanged")
        if (webChromeClient != null) {
            webChromeClient!!.onProgressChanged(view, newProgress)
        } else {
            super.onProgressChanged(view, newProgress)
        }
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        Log.d(TAG, "onReceivedTitle")
        if (webChromeClient != null) {
            webChromeClient!!.onReceivedTitle(view, title)
        } else {
            super.onReceivedTitle(view, title)
        }
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        Log.d(TAG, "onReceivedIcon")
        if (webChromeClient != null) {
            webChromeClient!!.onReceivedIcon(view, icon)
        } else {
            super.onReceivedIcon(view, icon)
        }
    }

    override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
        Log.d(TAG, "onReceivedTouchIconUrl")
        if (webChromeClient != null) {
            webChromeClient!!.onReceivedTouchIconUrl(view, url, precomposed)
        } else {
            super.onReceivedTouchIconUrl(view, url, precomposed)
        }
    }

    override fun onShowCustomView(view: View?, callback: IX5WebChromeClient.CustomViewCallback?) {
        Log.d(TAG, "onShowCustomView")
        if (webChromeClient != null) {
            webChromeClient!!.onShowCustomView(view, callback)
        } else {
            super.onShowCustomView(view, callback)
        }
    }

    override fun onHideCustomView() {
        Log.d(TAG, "onHideCustomView")
        if (webChromeClient != null) {
            webChromeClient!!.onHideCustomView()
        } else {
            super.onHideCustomView()
        }
    }

    override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
        Log.d(TAG, "onCreateWindow")
        return webChromeClient?.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                ?: super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onRequestFocus(view: WebView?) {
        Log.d(TAG, "onRequestFocus")
        if (webChromeClient != null) {
            webChromeClient!!.onRequestFocus(view)
        } else {
            super.onRequestFocus(view)
        }
    }

    override fun onCloseWindow(window: WebView?) {
        Log.d(TAG, "onCloseWindow")
        if (webChromeClient != null) {
            webChromeClient!!.onCloseWindow(window)
        } else {
            super.onCloseWindow(window)
        }
    }

    /**
     * 当网页调用alert()来弹出alert弹出框前回调，用以拦截alert()函数
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        Log.d(TAG, "onJsAlert")
        return webChromeClient?.onJsAlert(view, url, message, result)
                ?: super.onJsAlert(view, url, message, result)
    }

    /**
     * 当网页调用confirm()来弹出confirm弹出框前回调，用以拦截confirm()函数
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        Log.d(TAG, "onJsConfirm")
        return webChromeClient?.onJsConfirm(view, url, message, result)
                ?: super.onJsConfirm(view, url, message, result)
    }

    /**
     * 当网页调用prompt()来弹出prompt弹出框前回调，用以拦截prompt()函数
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
        Log.d(TAG, "onJsPrompt")
        return webChromeClient?.onJsPrompt(view, url, message, defaultValue, result)
                ?: super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        Log.d(TAG, "onJsBeforeUnload")
        return webChromeClient?.onJsBeforeUnload(view, url, message, result)
                ?: super.onJsBeforeUnload(view, url, message, result)
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissionsCallback) {
        Log.d(TAG, "onGeolocationPermissionsShowPrompt")
        if (webChromeClient != null) {
            webChromeClient!!.onGeolocationPermissionsShowPrompt(origin, callback)
        } else {
            super.onGeolocationPermissionsShowPrompt(origin, callback)
        }
    }

    override fun onGeolocationPermissionsHidePrompt() {
        Log.d(TAG, "onGeolocationPermissionsHidePrompt")
        if (webChromeClient != null) {
            webChromeClient!!.onGeolocationPermissionsHidePrompt()
        } else {
            super.onGeolocationPermissionsHidePrompt()
        }
    }

    /**
     * 打印 console 信息
     *
     * @param consoleMessage
     * @return
     */
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d(TAG, "onConsoleMessage")
        return webChromeClient?.onConsoleMessage(consoleMessage)
                ?: super.onConsoleMessage(consoleMessage)
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        Log.d(TAG, "getDefaultVideoPoster")
        return if (webChromeClient != null) {
            webChromeClient!!.defaultVideoPoster
        } else {
            super.getDefaultVideoPoster()
        }
    }

    override fun getVideoLoadingProgressView(): View? {
        Log.d(TAG, "getVideoLoadingProgressView")
        return if (webChromeClient != null) {
            webChromeClient!!.videoLoadingProgressView
        } else {
            super.getVideoLoadingProgressView()
        }
    }

    override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
        Log.d(TAG, "getVisitedHistory")
        if (webChromeClient != null) {
            webChromeClient!!.getVisitedHistory(callback)
        } else {
            super.getVisitedHistory(callback)
        }
    }

    companion object {

        private val TAG = X5WebChromeClient::class.java.simpleName
    }
}
