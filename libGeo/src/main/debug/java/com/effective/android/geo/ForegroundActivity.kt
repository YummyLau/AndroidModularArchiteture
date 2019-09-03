package com.effective.android.geo

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.DotOptions
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.model.LatLng


/**
 * 适配Android 8.0限制后台定位的功能，新增允许后台定位的接口，即开启一个前台定位服务
 */
class ForegroundActivity : Activity() {
    private var mClient: LocationClient? = null
    private val myLocationListener = MyLocationListener()

    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mForegroundBtn: Button? = null

    private var mNotificationUtils: NotificationUtils? = null
    private var notification: Notification? = null

    private var isFirstLoc = true
    private var isEnableLocInForeground = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foreground)

        initViews()

        // 定位初始化
        mClient = LocationClient(this)
        val mOption = LocationClientOption()
        mOption.setScanSpan(5000)
        mOption.setCoorType("bd09ll")
        mOption.setIsNeedAddress(true)
        mOption.isOpenGps = true
        mClient!!.locOption = mOption
        mClient!!.registerLocationListener(myLocationListener)
        mClient!!.start()

        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = NotificationUtils(this)
            val builder2 = mNotificationUtils!!.getAndroidChannelNotification("适配android 8限制后台定位功能", "正在后台定位")
            notification = builder2.build()
        } else {
            //获取一个Notification构造器
            val builder = Notification.Builder(this@ForegroundActivity)
            val nfIntent = Intent(this@ForegroundActivity, ForegroundActivity::class.java)

            builder.setContentIntent(PendingIntent.getActivity(this@ForegroundActivity, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

            notification = builder.build() // 获取构建好的Notification
        }
        notification!!.defaults = Notification.DEFAULT_SOUND //设置为默认的声音
    }


    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
        mMapView = null
        mClient!!.disableLocInForeground(true)
        mClient!!.unRegisterLocationListener(myLocationListener)
        mClient!!.stop()
    }


    private fun initViews() {
        mForegroundBtn = findViewById<View>(R.id.bt_foreground) as Button
        mForegroundBtn!!.setOnClickListener {
            if (isEnableLocInForeground) {
                //关闭后台定位（true：通知栏消失；false：通知栏可手动划除）
                mClient!!.disableLocInForeground(true)
                isEnableLocInForeground = false
                mForegroundBtn!!.setText(R.string.geo_startforeground)
            } else {
                //开启后台定位
                mClient!!.enableLocInForeground(1, notification)
                isEnableLocInForeground = true
                mForegroundBtn!!.setText(R.string.geo_stopforeground)
            }
        }
        mMapView = findViewById<View>(R.id.mv_foreground) as MapView
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.isMyLocationEnabled = true
    }


    internal inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            if (bdLocation == null || mMapView == null) {
                return
            }
            val locData = MyLocationData.Builder().accuracy(bdLocation.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.direction).latitude(bdLocation.latitude)
                    .longitude(bdLocation.longitude).build()
            // 设置定位数据
            mBaiduMap!!.setMyLocationData(locData)
            //地图SDK处理
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(bdLocation.latitude,
                        bdLocation.longitude)
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(18.0f)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
            val point = LatLng(bdLocation.latitude, bdLocation.longitude)
            val dotOption = DotOptions().center(point).color(-0x55010000)
            mBaiduMap!!.addOverlay(dotOption)
        }
    }

}
