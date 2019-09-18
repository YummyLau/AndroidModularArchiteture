package com.effective.android.component.blog.bean

import com.google.gson.annotations.SerializedName

/**
 * 文章实体
 * created by yummylau on 2019/09/18
 */
class Article {

    @SerializedName("apkLink")
    var apkLink: String? = null

    @SerializedName("audit")
    val audit: Int? = null

    @SerializedName("author")
    val author: String? = null

    @SerializedName("chapterId")
    val chapterId: Long? = null

    @SerializedName("chapterName")
    val chapterName: String? = null

    @SerializedName("collect")
    val collect: Boolean? = null

    @SerializedName("courseId")
    val courseId: Long? = null

    @SerializedName("desc")
    val desc: String? = null

    @SerializedName("envelopePic")
    val envelopePic: String? = null

    @SerializedName("fresh")
    val fresh: Boolean? = null

    @SerializedName("id")
    val id: Long? = null

    @SerializedName("link")
    val link: String? = null

    @SerializedName("niceDate")
    val niceDate: String? = null

    @SerializedName("niceShareDate")
    val niceShareDate: String? = null

    @SerializedName("origin")
    val origin: String? = null

    @SerializedName("prefix")
    val prefix: String? = null

    @SerializedName("projectLink")
    val projectLink: String? = null

    @SerializedName("publishTime")
    val publishTime: Long? = null

    @SerializedName("shareData")
    val shareData: String? = null

    @SerializedName("shareUser")
    val shareUser: String? = null

    @SerializedName("superChapterId")
    val superChapterId: Long? = null

    @SerializedName("superChapterName")
    val superChapterName: String? = null

    @SerializedName("tags")
    val tags: List<Tag>? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("type")
    val type: Int? = null

    @SerializedName("userId")
    val userId: Long? = null

    @SerializedName("visible")
    val visible: Int? = null

    @SerializedName("zan")
    val zan: Int? = null
}