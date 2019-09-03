package com.effective.android.video

import android.content.Context
import android.net.ConnectivityManager
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.effective.android.video.bean.VideoInfo
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

object NetUtil {
    fun isWifi(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            return if (ni != null) {
                ni.type == ConnectivityManager.TYPE_WIFI
            } else {
                false
            }
        } catch (e: Exception) {
            return false
        }
    }
}

object DataFactory {

    fun getListDatas(): MutableList<TestData> {
        val datas = ArrayList<TestData>()
        for (i in 1..100) {
            datas.add(
                    if (i % 3 == 0) {
                        TestData(null, "这是一个纯文本信息流")
                    } else if (i % 2 == 1) {
                        TestData(VideoInfo(
                                "https://ok.166.net/reunionpub/pr_u0odcramfcw3zpm-uapnsw==_51_1531895787_6822",
                                "https://ok.166.net/reunionpub/pr_u0odcramfcw3zpm-uapnsw==_51_1531895787_6822?vframe&offset=0&type=webp&resize=320x568",
                                320,
                                568,
                                10000), "这是一个竖屏视频信息流")
                    } else {
                        TestData(VideoInfo(
                                "https://vod.cc.163.com/file/5ceba38e6a37ca858b965a77.mp4?client_type=ios&protocol=https&source=23",
                                "http://nbot-common.nos-jd.163yun.com/20a56e686daa485843df9ce3555d648b.jpg",
                                0,
                                0,
                                37000), "这是一个横屏视频信息流")
                    }

            )
        }
        return datas
    }

    fun getStreamDatas(): MutableList<TestData> {
        val datas = ArrayList<TestData>()
        for (i in 1..100) {
            datas.add(
                    if (i % 2 == 0) {
                        TestData(VideoInfo(
                                "https://ok.166.net/reunionpub/pr_u0odcramfcw3zpm-uapnsw==_51_1531895787_6822",
                                "https://ok.166.net/reunionpub/pr_u0odcramfcw3zpm-uapnsw==_51_1531895787_6822?vframe&offset=0&type=webp&resize=320x568",
                                320,
                                568,
                                10000), "这是一个竖屏视频信息流")
                    } else {
                        TestData(VideoInfo(
                                "https://vod.cc.163.com/file/5ceba38e6a37ca858b965a77.mp4?client_type=ios&protocol=https&source=23",
                                "http://nbot-common.nos-jd.163yun.com/20a56e686daa485843df9ce3555d648b.jpg",
                                0,
                                0,
                                37000), "这是一个横屏视频信息流")
                    }
            )
        }
        return datas
    }
}

@Parcelize
data class TestData(var videoInfo: VideoInfo?, val itemName: String) : Parcelable