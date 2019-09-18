package com.effective.android.service.net

import com.google.gson.annotations.SerializedName

/**
 * 列表数据封装
 * created by yummylau on 2019/09/18
 */
class ListData<T> {

    @SerializedName("curPage")
    val curPage: Int = 0

    @SerializedName("offset")
    val offset: Int = 0

    @SerializedName("over")
    val over: Boolean = false

    @SerializedName("pageCount")
    val pageCount: Int = 0

    @SerializedName("size")
    val size: Int = 0

    @SerializedName("total")
    val total: Int = 0

    @SerializedName("datas")
    val datas: List<T>? = null

}