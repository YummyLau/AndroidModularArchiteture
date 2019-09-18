package com.effective.android.component.blog.bean

import com.google.gson.annotations.SerializedName

/**
 * banner实体
 * created by yummylau on 2019/09/18
 */
class Banner {

    @SerializedName("desc")
    var desc: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("imagePath")
    var imagePath: String? = null

    @SerializedName("isVisible")
    var isVisible: Int? = null

    @SerializedName("order")
    var order: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("type")
    var type: Int? = null

    @SerializedName("url")
    var url: String? = null
}