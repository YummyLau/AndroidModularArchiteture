package com.effective.android.geo

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import kotlinx.android.synthetic.main.hotwifi.*

/***
 * 判断移动热点示例
 * @author baidu
 */

class IsHotWifiActivity : Activity() {

    private var listener: MyLocationListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotwifi)
        listener = MyLocationListener()
        GeoManager.registerListener(listener)
        GeoManager.start()
        start!!.setOnClickListener {
            val b = GeoManager.requestHotSpotState()
            hotwifiResult!!.text = ""
        }
    }


    /***
     * 定位结果回调，在此方法中处理定位结果
     */

    internal inner class MyLocationListener : BDAbstractLocationListener() {

        override fun onConnectHotSpotMessage(s: String?, i: Int) {
            var resText = ""
            if (i == 0) {
                resText = "不是wifi热点, wifi:" + s!!
            } else if (i == 1) {
                resText = "是wifi热点, wifi:" + s!!
            } else if (i == -1) {
                resText = "未连接wifi"
            }
            hotwifiResult!!.text = resText
        }

        override fun onReceiveLocation(arg0: BDLocation) {
            // TODO Auto-generated method stub
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        GeoManager.unregisterListener(listener)
        GeoManager.stop()
    }
}
