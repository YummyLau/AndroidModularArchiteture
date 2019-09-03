package com.effective.android.webview

import android.content.Context
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import java.util.HashMap

/**
 * 模块入口
 */
object X5WebViewManager{

    var dataBasePath : String ? = null
    var appCachePath : String ? = null

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
    @JvmStatic
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
