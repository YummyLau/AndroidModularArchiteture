package com.effective.android.geo

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.RelativeLayout

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.MapBaseIndoorMapInfo
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.effective.android.geo.indoorview.BaseStripAdapter
import com.effective.android.geo.indoorview.StripListView
import kotlinx.android.synthetic.main.activity_location.*

/**
 * 此demo用来展示如何结合定位SDK实现室内定位，并使用MyLocationOverlay绘制定位位置
 */
class IndoorLocationActivity : Activity() {

    // 定位相关
    internal var mLocClient: LocationClient? = null
    var myListener = MyLocationListenner()
    private var mCurrentMode: LocationMode? = null
    internal var mCurrentMarker: BitmapDescriptor? = null

    internal var mMapView: MapView? = null
    internal var mBaiduMap: BaiduMap? = null

    internal var stripListView: StripListView? = null
    internal var mFloorListAdapter: BaseStripAdapter? = null
    internal var mMapBaseIndoorMapInfo: MapBaseIndoorMapInfo? = null
    // UI相关
    internal var isFirstLoc = true // 是否首次定位


    companion object {
        internal var mContext: Context? = null
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val layout = RelativeLayout(this)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mainview = inflater.inflate(R.layout.activity_location, null)
        layout.addView(mainview)

        mCurrentMode = LocationMode.NORMAL
        button1!!.text = "普通"
        val btnClickListener = OnClickListener {
            when (mCurrentMode) {
                MyLocationConfiguration.LocationMode.NORMAL -> {
                    button1!!.text = "跟随"
                    mCurrentMode = LocationMode.FOLLOWING
                    mBaiduMap!!.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                MyLocationConfiguration.LocationMode.COMPASS -> {
                    button1!!.text = "普通"
                    mCurrentMode = LocationMode.NORMAL
                    mBaiduMap!!.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                MyLocationConfiguration.LocationMode.FOLLOWING -> {
                    button1!!.text = "罗盘"
                    mCurrentMode = LocationMode.COMPASS
                    mBaiduMap!!.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                else -> {
                }
            }
        }
        button1!!.setOnClickListener(btnClickListener)

        // 地图初始化
        mMapView = mainview.findViewById<View>(R.id.bmapView) as MapView
        mBaiduMap = mMapView!!.map
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        // 开启室内图
        mBaiduMap!!.setIndoorEnable(true)
        // 定位初始化
        mLocClient = LocationClient(this)
        mLocClient!!.registerLocationListener(myListener)
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(3000)
        mLocClient!!.locOption = option
        mLocClient!!.start()

        stripListView = StripListView(this)
        layout.addView(stripListView)
        setContentView(layout)
        mFloorListAdapter = BaseStripAdapter(this@IndoorLocationActivity)

        mBaiduMap!!.setOnBaseIndoorMapListener(BaiduMap.OnBaseIndoorMapListener { b, mapBaseIndoorMapInfo ->
            if (b == false || mapBaseIndoorMapInfo == null) {
                stripListView!!.visibility = View.INVISIBLE

                return@OnBaseIndoorMapListener
            }

            mFloorListAdapter!!.setmFloorList(mapBaseIndoorMapInfo.floors)
            stripListView!!.visibility = View.VISIBLE
            stripListView!!.setStripAdapter(mFloorListAdapter)
            mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo
        })

    }


    /**
     * 定位SDK监听函数
     */
    inner class MyLocationListenner : BDAbstractLocationListener() {

        private var lastFloor: String? = null

        override fun onReceiveLocation(location: BDLocation?) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }
            val bid = location.buildingID
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.i("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo!!.id)
                if (bid == mMapBaseIndoorMapInfo!!.id) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    val floor = location.floor.toUpperCase()// 楼层
                    Log.i("indoor", "floor = " + floor + " position = " + mFloorListAdapter!!.getPosition(floor))
                    Log.i("indoor", "radius = " + location.radius + " type = " + location.networkLocationType)

                    var needUpdateFloor = true
                    if (lastFloor == null) {
                        lastFloor = floor
                    } else {
                        if (lastFloor == floor) {
                            needUpdateFloor = false
                        } else {
                            lastFloor = floor
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap!!.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo!!.id)
                        mFloorListAdapter!!.setSelectedPostion(mFloorListAdapter!!.getPosition(floor))
                        mFloorListAdapter!!.notifyDataSetInvalidated()
                    }

                    if (!location.isIndoorLocMode) {
                        mLocClient!!.startIndoorMode()// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.i("indoor", "start indoormod")
                    }
                }
            }

            val locData = MyLocationData.Builder().accuracy(location.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100f).latitude(location.latitude).longitude(location.longitude).build()
            mBaiduMap!!.setMyLocationData(locData)
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(location.latitude, location.longitude)
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(18.0f)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
        }

        fun onReceivePoi(poiLocation: BDLocation) {}

        override fun onConnectHotSpotMessage(s: String?, i: Int) {}
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        mMapView!!.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        // 退出时销毁定位
        mLocClient!!.stop()
        // 关闭定位图层
        mBaiduMap!!.isMyLocationEnabled = false
        mMapView!!.onDestroy()
        mMapView = null
        super.onDestroy()
    }
}
