package com.effective.android.geo

import android.content.Context
import com.baidu.location.BDLocation
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiNearbySearchOption
import com.baidu.mapapi.search.poi.PoiSearch
import com.baidu.mapapi.search.poi.PoiSortType

/**
 * 地图模块管理
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
object GeoManager {

    private const val FIRST_LOACTION_DURATION = 0

    @Volatile
    var sBDLocation: BDLocation? = null

    fun init(applicationContext: Context) {
        try {
            SDKInitializer.initialize(applicationContext)
            SDKInitializer.setCoordType(CoordType.GCJ02)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 配置百度sdk参数
     */
    fun getDefaultLocationClientOption(): LocationClientOption {
        val locationClientOption = LocationClientOption()
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationClientOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationClientOption.setCoorType("gcj02")
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        locationClientOption.setScanSpan(FIRST_LOACTION_DURATION)
        //可选，设置是否需要地址信息，默认不需要
        locationClientOption.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        locationClientOption.setIsNeedLocationDescribe(true)
        //可选，设置是否需要设备方向结果
        locationClientOption.setNeedDeviceDirect(true)
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationClientOption.isLocationNotify = true
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClientOption.setIgnoreKillProcess(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationClientOption.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationClientOption.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationClientOption.SetIgnoreCacheException(false)
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationClientOption.setIsNeedAltitude(false)
        return locationClientOption
    }


    /**
     * 给定一个坐标和关键字，返回搜素结果。
     * 获取 PoiSearch 的对象在页面销毁的时候需要调用其 destory 方法回收
     **/
    fun searchNearby(bdLocation: BDLocation, keyword: String, onGetPoiSearchResultListener: OnGetPoiSearchResultListener): PoiSearch {
        val poiSearch = PoiSearch.newInstance()
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener)
        val center = LatLng(bdLocation.latitude, bdLocation.longitude)
        val radius = 10000
        poiSearch.searchNearby(PoiNearbySearchOption().keyword(keyword ?: "")
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(center)
                .radius(radius)
                .pageNum(10))
        return poiSearch
    }
}