package com.effective.android.component.square.bean

import com.google.gson.annotations.SerializedName

/**
 * 搜索关键词实体
 * created by yummylau on 2019/09/18
 */
class SearchKey {

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("link")
    var link: String? = null

    @SerializedName("order")
    var order: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("visible")
    var visible: Int? = null
}