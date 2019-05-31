package com.effective.android.video.bean

/**
 * 简单定义一个视频的信息，不同业务自行扩展
 */
class VideoInfo {

    var url: String? = null
    var cover: String? = null
    var width: Int = 0
    var height: Int = 0
    var duration: Long = 0

}
