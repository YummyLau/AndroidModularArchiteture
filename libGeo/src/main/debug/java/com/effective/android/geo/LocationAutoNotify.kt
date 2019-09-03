package com.effective.android.geo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.baidu.location.LocationClientOption
import com.baidu.location.LocationClientOption.LocationMode
import com.effective.android.geo.location.LocationActivity
import com.effective.android.geo.service.LocationService
import kotlinx.android.synthetic.main.autonotifyconfig.*

/***
 * 展示定位sdk自定义定位模式，注意！设置setOpenAutoNotifyMode后,setScanSpan会失效，回调由setOpenAutoNotifyMode内设置的数值决定
 * @author baidu
 */
class LocationAutoNotify : AppCompatActivity() {
    private var locService: LocationService? = null
    private var mOption: LocationClientOption? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.autonotifyconfig)
        mOption = GeoManager.defaultLocationClientOption
        mOption!!.setOpenAutoNotifyMode()
    }

    override fun onResume() {
        super.onResume()
        autonotify_start.setOnClickListener {
            // TODO Auto-generated method stub
            var setFrequence = 60 * 1000
            var setDistance = 100
            var setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT

            when (autonotify_selectMode!!.checkedRadioButtonId) {
                R.id.radio_hight -> setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT
                R.id.radio_low -> {
                    mOption!!.locationMode = LocationMode.Battery_Saving
                    setSensitivity = LocationClientOption.LOC_SENSITIVITY_MIDDLE
                }
                R.id.radio_device -> {
                    mOption!!.locationMode = LocationMode.Device_Sensors
                    setSensitivity = LocationClientOption.LOC_SENSITIVITY_LOW
                }
                else -> {
                }
            }
            try {
                setDistance = Integer.parseInt(autonotify_distance!!.text.toString())
            } catch (e: Exception) {
                setDistance = 100
            }

            try {
                setFrequence = Integer.parseInt(autonotify_time!!.text.toString())
            } catch (e: Exception) {
                setFrequence = 60 * 1000
            }

            /**
             * 设置前需停止定位服务，设置后重启定位服务才可以生效
             */

            /**
             * 设置前需停止定位服务，设置后重启定位服务才可以生效
             */
            mOption!!.setOpenAutoNotifyMode(setFrequence, setDistance, setSensitivity)
            locService!!.setLocationOption(mOption)
            val locIntent = Intent(this@LocationAutoNotify, LocationActivity::class.java)
            locIntent.putExtra("from", 1)
            this@LocationAutoNotify.startActivity(locIntent)
        }
    }

}
