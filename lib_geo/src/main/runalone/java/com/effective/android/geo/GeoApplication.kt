package com.effective.android.geo


import android.app.Application
import android.app.Service
import android.os.Vibrator

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */
class GeoApplication : Application() {

    var mVibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        GeoManager.init(this)
        mVibrator = applicationContext.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }
}
