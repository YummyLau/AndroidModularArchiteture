package com.effective.android.component.blog.bean

import com.google.gson.annotations.SerializedName

/**
 * 网站
 * created by yummylau on 2019/09/18
 */
class Website {

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("link")
    var link: String? = null

    @SerializedName("visible")
    var visible: Int? = null

    @SerializedName("order")
    var order: Int? = null

    @SerializedName("name")
    var name: String? = null
}