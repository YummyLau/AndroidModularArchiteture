package com.effective.android.video.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 简单定义一个视频的信息，不同业务自行扩展
 */
@Parcelize
data class VideoInfo(var url: String, var cover: String, var width: Int = 0, var height: Int = 0, var duration: Long = 0) : Parcelable
