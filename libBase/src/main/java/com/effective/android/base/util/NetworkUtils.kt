package com.effective.android.base.util

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

import androidx.annotation.RequiresPermission

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
object NetworkUtils {

    val NETWORKTYPE_INVALID = 0    // 无网络
    val NETWORKTYPE_2G = 1            // 2G
    val NETWORKTYPE_3G = 2            // 3G
    val NETWORKTYPE_4G = 3         // 4G
    val NETWORKTYPE_WIFI = 4        // WIFI
    val NETWORKTYPE_UNKNOWN = 5    // 未知手机网络


    @RequiresPermission(allOf =[ACCESS_NETWORK_STATE])
    fun getNetworkType(context: Context?): Int {
        var type = NETWORKTYPE_INVALID
        try {
            if (context != null) {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        type = NETWORKTYPE_WIFI
                    } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                        val _strSubTypeName = networkInfo.subtypeName

                        // TD-SCDMA   networkType is 17
                        val networkType = networkInfo.subtype
                        when (networkType) {
                            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN   //api<8 : replace by 11
                            -> type = NETWORKTYPE_2G
                            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B //api<9 : replace by 14
                                , TelephonyManager.NETWORK_TYPE_EHRPD  //api<11 : replace by 12
                                , TelephonyManager.NETWORK_TYPE_HSPAP  //api<13 : replace by 15
                            -> type = NETWORKTYPE_3G
                            TelephonyManager.NETWORK_TYPE_LTE    //api<11 : replace by 13
                            -> type = NETWORKTYPE_4G
                            else ->
                                // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                                if (_strSubTypeName.equals("TD-SCDMA", ignoreCase = true) || _strSubTypeName.equals(
                                                "WCDMA", ignoreCase = true) || _strSubTypeName.equals("CDMA2000", ignoreCase = true)) {
                                    type = NETWORKTYPE_3G
                                } else {
                                    type = NETWORKTYPE_UNKNOWN
                                }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return type
    }
}
