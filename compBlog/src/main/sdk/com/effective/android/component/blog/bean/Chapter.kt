package com.effective.android.component.blog.bean

import com.google.gson.annotations.SerializedName

open class Chapter{

    @SerializedName("children")
    var children: MutableList<Chapter> = mutableListOf()

    @SerializedName("courseId")
    var courseId: Int = -1

    @SerializedName("id")
    var id: Long = -1

    @SerializedName("name")
    var name: String = ""

    @SerializedName("order")
    var order: Int? = -1

    @SerializedName("parentChapterId")
    var parentChapterId: Long = -1

    @SerializedName("userControlSetTop")
    var userControlSetTop: Boolean = false

    @SerializedName("visible")
    var visible: Int = -1
}