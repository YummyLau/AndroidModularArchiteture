package com.effective.android.component.blog.bean

import com.google.gson.annotations.SerializedName

/**
 * 文章tag实体
 * created by yummylau on 2019/09/18
 */
class Tag {

    @SerializedName("name")
    var name:String? = null

    @SerializedName("url")
    var url:String? = null
}