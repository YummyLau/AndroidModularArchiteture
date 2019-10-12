package com.effective.android.service.kit.data

import android.os.Parcel
import android.os.Parcelable
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.service.kit.Constants
import com.google.gson.annotations.SerializedName

open class Article : IMediaItem{

    override fun getType(): Int = Constants.articleType

    override fun getViewType(): Int = Constants.articleType

    override fun replace(item: IMediaItem) {
        if (item is Article) {
            apkLink = item.apkLink
            audit = item.audit
            author = item.author
            chapterId = item.chapterId
            chapterName = item.chapterName
            collect = item.collect
            courseId = item.courseId
            desc = item.desc
            envelopePic = item.envelopePic
            fresh = item.fresh
            id = item.id
            link = item.link
            niceDate = item.niceDate
            niceShareDate = item.niceShareDate
            origin = item.origin
            prefix = item.prefix
            projectLink = item.projectLink
            publishTime = item.publishTime
            shareData = item.shareData
            shareUser = item.shareUser
            superChapterId = item.superChapterId
            superChapterName = item.superChapterName
            tags = item.tags
            title = item.title
            type = item.type
            userId = item.userId
            visible = item.visible
            zan = item.zan
        }
    }

    override fun isContentSame(item: IMediaItem): Boolean = isIdSame(item)

    override fun isIdSame(item: IMediaItem): Boolean {
        return item is Article && item.id == id
    }


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