package com.effective.android.webview

import android.content.Context
import android.util.AttributeSet

import com.effective.android.webview.interfaces.OnScrollChangeCallback
import com.effective.android.webview.jsbridge.BridgeWebViewClient
import com.effective.android.webview.interfaces.OnFinishCallback
import com.effective.android.webview.jsbridge.BridgeWebViewKotlin
import com.tencent.smtt.sdk.WebViewClient

/**
 * 1. addJavascriptInterface 4.2以下存在漏洞
 * 2. WebView 密码明文存储漏洞 /data/data/com.package.name/databases/webview.db
 * 1. webView耗电的问题，我们之前发现的一个情况是，webView切换到后台时，如果当前页面有JS代码仍在不时的run, 就会导致比较严重的耗电，所以必须确保切换到后台后暂停JS执行，同时切回来的时候恢复它
 * 2. 闪屏问题，和硬件加速有关
 * 3. 选择文件的兼容问题 https://www.jianshu.com/p/48e688ce801f
 *
 *
 * 解决问题：通过是否是用户点击来判断是否处理重定向问题
 * Created by yummylau on 2018/5/7.
 */
class X5JsWebView : BridgeWebViewKotlin {

    private var mOnScrollChangedCallback: OnScrollChangeCallback? = null
    private var bridgeWebViewClient: BridgeWebViewClient? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun generateBridgeWebViewClient(): BridgeWebViewClient {
        if (bridgeWebViewClient == null) {
            bridgeWebViewClient = BridgeWebViewClient(this)
        }
        return bridgeWebViewClient!!
    }

    fun setWebViewClientProxy(webViewClient: WebViewClient) {
        generateBridgeWebViewClient().setProxy(webViewClient)
    }

    fun setOnScrollChangedCallback(
            onScrollChangedCallback: OnScrollChangeCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback
    }

    fun setOnFinishCallback(mOnFinishCallback: OnFinishCallback) {
        generateBridgeWebViewClient().setOnFinishCallback(mOnFinishCallback)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback!!.onScroll(l, t, oldl, oldt)
        }
    }

    override fun postUrl(s: String, bytes: ByteArray) {
        if (Utils.isTrustUrl(s)) {
            super.postUrl(s, bytes)
        } else {
            super.postUrl(s, null)
        }
    }
}
