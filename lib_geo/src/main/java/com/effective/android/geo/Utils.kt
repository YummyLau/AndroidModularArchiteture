package com.effective.android.geo

import com.baidu.location.BDLocation

/**
 * 工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
object Utils {

    /**
     * 是否时合法的坐标
     */
    fun isLegal(dDLocation: BDLocation?): Boolean {
        return (dDLocation != null
                && dDLocation.latitude != 4.9E-324
                && dDLocation.longitude != 4.9E-324)
    }
}