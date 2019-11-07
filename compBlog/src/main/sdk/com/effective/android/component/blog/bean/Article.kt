package com.effective.android.component.blog.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Article : Parcelable {

    @SerializedName("apkLink")
    var apkLink: String? = null

    @SerializedName("audit")
    var audit: Int? = null

    @SerializedName("author")
    var author: String? = null

    @SerializedName("chapterId")
    var chapterId: Long? = null

    @SerializedName("chapterName")
    var chapterName: String? = null

    @SerializedName("collect")
    var collect: Boolean? = null

    @SerializedName("courseId")
    var courseId: Long? = null

    @SerializedName("desc")
    var desc: String? = null

    @SerializedName("envelopePic")
    var envelopePic: String? = null

    @SerializedName("fresh")
    var fresh: Boolean? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("link")
    var link: String? = null

    @SerializedName("niceDate")
    var niceDate: String? = null

    @SerializedName("niceShareDate")
    var niceShareDate: String? = null

    @SerializedName("origin")
    var origin: String? = null

    @SerializedName("prefix")
    var prefix: String? = null

    @SerializedName("projectLink")
    var projectLink: String? = null

    @SerializedName("publishTime")
    var publishTime: Long? = null

    @SerializedName("shareData")
    var shareData: String? = null

    @SerializedName("shareUser")
    var shareUser: String? = null

    @SerializedName("superChapterId")
    var superChapterId: Long? = null

    @SerializedName("superChapterName")
    var superChapterName: String? = null

    @SerializedName("tags")
    var tags: List<Tag>? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("type")
    var type: Int? = null

    @SerializedName("userId")
    var userId: Long? = null

    @SerializedName("visible")
    var visible: Int? = null

    @SerializedName("zan")
    var zan: Int? = null
}