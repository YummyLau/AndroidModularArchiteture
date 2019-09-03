package com.effective.android.geo

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer

object GeoManager {

    private var mClient: LocationClient? = null
    private var mOption: LocationClientOption? = null
    private var mDiyoption: LocationClientOption? = null
    private val mLock = Any()

    fun init(context: Context) {
        SDKInitializer.initialize(context.applicationContext)
        mClient = LocationClient(context.applicationContext)
    }

    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
    //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
    //可选，设置是否需要地址信息，默认不需要
    //可选，设置是否需要地址描述
    //可选，设置是否需要设备方向结果
    //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
    //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    //可选，默认false，设置是否收集CRASH信息，默认收集
    //可选，默认false，设置是否开启Gps定位
    //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
    val defaultLocationClientOption: LocationClientOption
        get() {
            if (mOption == null) {
                mOption = LocationClientOption()
                mOption!!.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
                mOption!!.setCoorType("bd09ll")
                mOption!!.setScanSpan(3000)
                mOption!!.setIsNeedAddress(true)
                mOption!!.setIsNeedLocationDescribe(true)
                mOption!!.setNeedDeviceDirect(false)
                mOption!!.isLocationNotify = false
                mOption!!.setIgnoreKillProcess(true)
                mOption!!.setIsNeedLocationDescribe(true)
                mOption!!.setIsNeedLocationPoiList(true)
                mOption!!.SetIgnoreCacheException(false)
                mOption!!.isOpenGps = true
                mOption!!.setIsNeedAltitude(false)

            }
            return mOption!!
        }

    val diyOption: LocationClientOption
        get() {
            if (mDiyoption == null) {
                mDiyoption = LocationClientOption()
            }
            return mDiyoption!!
        }

    val isStart: Boolean
        get() = mClient!!.isStarted

    fun registerListener(listener: BDAbstractLocationListener?): Boolean {
        var isSuccess = false
        if (listener != null) {
            mClient!!.registerLocationListener(listener)
            isSuccess = true
        }
        return isSuccess
    }

    fun unregisterListener(listener: BDAbstractLocationListener?) {
        if (listener != null) {
            mClient!!.unRegisterLocationListener(listener)
        }
    }

    fun setLocationOption(option: LocationClientOption?): Boolean {
        val isSuccess = false
        if (option != null) {
            if (mClient!!.isStarted)
                mClient!!.stop()
            mDiyoption = option
            mClient!!.locOption = option
        }
        return isSuccess
    }

    fun start() {
        synchronized(mLock) {
            if (mClient != null && !mClient!!.isStarted) {
                mClient!!.start()
            }
        }
    }

    fun stop() {
        synchronized(mLock) {
            if (mClient != null && mClient!!.isStarted) {
                mClient!!.stop()
            }
        }
    }

    fun requestHotSpotState(): Boolean {
        return mClient!!.requestHotSpotState()
    }

}
