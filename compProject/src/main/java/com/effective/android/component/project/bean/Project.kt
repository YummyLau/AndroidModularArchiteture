package com.effective.android.component.project.bean

import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.project.Constants
import com.google.gson.annotations.SerializedName

class Project{

    @SerializedName("children")
    var children: MutableList<Project> = mutableListOf()

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