package com.effective.android.webview

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import com.effective.android.webview.bean.Request
import com.effective.android.webview.bean.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener

import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import java.lang.reflect.Type

import java.net.URL
import java.util.HashMap

/**
 * 工具类
 * Created by yummylau on 2018/5/7.
 */
object Utils {

    @JvmStatic
    fun <T> object2Json(obj: T): String {
        return try {
            Gson().toJson(obj, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            ""
        }
    }

    @JvmStatic
    fun <T> json2Obj(json: String, type: Type): T? {
        return try {
            Gson().fromJson<T>(json, type)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun hookKeyCode(webView: WebView, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()//返回上一页面
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun compatVideoPlay(activity: Activity) {
        val window = activity.window
        window.setFormat(PixelFormat.TRANSLUCENT)
        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                window.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
            }
        } catch (e: Exception) {
        }

    }

    @JvmStatic
    fun destroyWebView(webView: X5JsWebView?) {
        if (webView != null) {
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            webView.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.settings.javaScriptEnabled = false

            webView.clearHistory()
            webView.clearCache(true)
            //            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.freeMemory()
            //            webView.pauseTimers();
            webView.removeAllViews()
            webView.destroy()
        }
    }

    @JvmStatic
    fun setDefaultWebViewSetting(webView: X5JsWebView?) {
        if (webView == null) {
            return
        }
        //初始化setting
        val settings = webView.settings
        settings.allowFileAccess = true
        settings.javaScriptEnabled = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.useWideViewPort = true
        settings.lightTouchEnabled = true
        settings.savePassword = true

        /**
         * 混合网页,比如https链接中嵌入http页面
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        settings.loadWithOverviewMode = true
        settings.saveFormData = false
        settings.setGeolocationEnabled(true)
        settings.javaScriptCanOpenWindowsAutomatically = true
        val webViewExtension = webView.x5WebViewExtension
        webViewExtension?.setScrollBarFadingEnabled(false)
        /**
         * 设置webview缓存
         */
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        /**
         * 存储一些简单的key/value数据，Session Storage（页面关闭就消失） 和 Local Storage（本地存储，永不过期）
         */
        settings.domStorageEnabled = true
        settings.databaseEnabled = false
        settings.databasePath = X5WebViewManager.dataBasePath


        /**
         * Application Caches 缓存目录， 能够缓存web浏览器中所有东西，从页面图片到脚本css等。大小通常为5m
         * webkie使用一个db文件保存appcache缓存的数据
         */
        settings.setAppCacheEnabled(false)
        settings.setAppCachePath(X5WebViewManager.appCachePath)
        //        settings.setAppCacheMaxSize();

        /**
         * WebView先不要自动加载图片，等页面finish后再发起图片加载
         */
        settings.loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19
        setUA(webView)
    }

    @JvmStatic
    fun setUA(webView: WebView?) {
        if (webView == null) {
            return
        }
        if (webView.settings == null) {
            return
        }
        var ua = webView.settings.userAgentString
        val version = getAppVersionName(webView.context, webView.context.packageName)
        ua = "$ua cusApp/$version"
        webView.settings.userAgentString = ua
    }

    @JvmStatic
    fun getAppVersionName(context: Context, packageName: String): String? {
        try {
            val info = context.packageManager.getPackageInfo(packageName, 0)
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }


    /**
     * 1、WebView:postUrl()前检测url的合法性
     * 2、Js调用Native方法前检测当前界面url的合法性
     *
     * @param url
     * @return
     */
    @JvmStatic
    fun isTrustUrl(url: String?): Boolean {
        if (!TextUtils.isEmpty(url)) {
            try {
                val realUrl = URL(url)
                if (true) {
                    //添加校验url逻辑
                    return true
                }
            } catch (e: Exception) {
                return false
            }

        }
        return false
    }

    @JvmStatic
    fun isTrustUrl(webView: X5JsWebView?): Boolean {
        val url = webView?.url
        return isTrustUrl(url)
    }

    /**
     * 多进程初始化方案
     * 在AndroidManifest.xml中增加内核首次加载时优化Service声明；    该Service仅在TBS内核首次Dex加载时触发并执行dex2oat任务，任务完成后自动结束。
     * <service android:name="com.tencent.smtt.export.external.DexClassLoaderProviderService" android:label="dexopt" android:process=":dexopt"></service>
     *
     *
     * 优先去共享手q微信的内核，如果没有则会下载，通过setTasListener监听下载状态
     *
     * @param context
     */
    fun initX5Webkit(context: Context) {
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        QbSdk.initTbsSettings(map)
        //监听下载进度
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                Log.d("X5init", "onDownloadFinish : $i")
            }

            override fun onInstallFinish(i: Int) {
                Log.d("X5init", "onInstallFinish : $i")
            }

            override fun onDownloadProgress(i: Int) {
                Log.d("X5init", "onDownloadProgress : $i")
            }
        })
        //qbsdk与加载，需要在wifi条件下下载x5内核，耗时90秒。如果打开webview是没有x5内核则默认使用系统内核
        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d("X5init", "onCoreInitFinished")
            }

            override fun onViewInitFinished(b: Boolean) {
                Log.d("X5init", "onViewInitFinished : $b")
            }
        })
    }
}
