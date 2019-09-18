package com.effective.android.service.net

import com.google.gson.annotations.SerializedName

/**
 * 列表数据封装
 * created by yummylau on 2019/09/18
 */
class ListData<T> {

    @SerializedName("curPage")
    var curPage: Int = 0

    @SerializedName("offset")
    var offset: Int = 0

    @SerializedName("over")
    var over: Boolean = false

    @SerializedName("pageCount")
    var pageCount: Int = 0

    @SerializedName("size")
    var size: Int = 0

    @SerializedName("total")
    var total: Int = 0

    @SerializedName("datas")
    var data: MutableList<T> = mutableListOf()

}