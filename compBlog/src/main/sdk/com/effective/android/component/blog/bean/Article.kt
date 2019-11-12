package com.effective.android.component.blog.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Article : Parcelable {

    @SerializedName("apkLink")
    var apkLink: String = ""

    @SerializedName("audit")
    var audit: Int = -1

    @SerializedName("author")
    var author: String = ""

    @SerializedName("chapterId")
    var chapterId: Long = -1

    @SerializedName("chapterName")
    var chapterName: String = ""

    @SerializedName("collect")
    var collect: Boolean = false

    @SerializedName("courseId")
    var courseId: Long = -1

    @SerializedName("desc")
    var desc: String = ""

    @SerializedName("envelopePic")
    var envelopePic: String = ""

    @SerializedName("fresh")
    var fresh: Boolean = false

    @SerializedName("id")
    var id: Long = -1

    @SerializedName("link")
    var link: String = ""

    @SerializedName("niceDate")
    var niceDate: String = ""

    @SerializedName("niceShareDate")
    var niceShareDate: String = ""

    @SerializedName("origin")
    var origin: String = ""

    @SerializedName("prefix")
    var prefix: String = ""

    @SerializedName("projectLink")
    var projectLink: String = ""

    @SerializedName("publishTime")
    var publishTime: Long = -1

    @SerializedName("shareData")
    var shareData: String = ""

    @SerializedName("shareUser")
    var shareUser: String = ""

    @SerializedName("superChapterId")
    var superChapterId: Long = -1

    @SerializedName("superChapterName")
    var superChapterName: String = ""

    @SerializedName("tags")
    var tags: List<Tag>? = null

    @SerializedName("title")
    var title: String = ""

    @SerializedName("type")
    var type: Int = -1

    @SerializedName("userId")
    var userId: Long = -1

    @SerializedName("visible")
    var visible: Int = -1

    @SerializedName("zan")
    var zan: Int = -1

    constructor()

    constructor(source: Parcel) {
        apkLink = source.readString()
        audit = source.readInt()
        author = source.readString()
        chapterId = source.readLong()
        chapterName = source.readString()
        collect = source.readInt() != 0
        courseId = source.readLong()
        desc = source.readString()
        envelopePic = source.readString()
        fresh = source.readInt() != 0
        id = source.readLong()
        link = source.readString()
        niceDate = source.readString()
        niceShareDate = source.readString()
        origin = source.readString()
        prefix = source.readString()
        projectLink = source.readString()
        publishTime = source.readLong()
        shareData = source.readString()
        shareUser = source.readString()
        superChapterId = source.readLong()
        superChapterName = source.readString()

        val array: ArrayList<Tag> = ArrayList()
        source.readTypedList(array, Tag.CREATOR)
        tags = array.toMutableList()

        title = source.readString()
        type = source.readInt()
        userId = source.readLong()
        visible = source.readInt()
        zan = source.readInt()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
            override fun createFromParcel(source: Parcel): Article {
                return Article(source)
            }

            override fun newArray(size: Int): Array<Article> {
                return Array(size) { Article() }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(apkLink)
        dest?.writeInt(audit)
        dest?.writeString(author)
        dest?.writeLong(chapterId)
        dest?.writeString(chapterName)
        dest?.writeInt(if(collect) 1 else 0)
        dest?.writeLong(courseId)
        dest?.writeString(desc)
        dest?.writeString(envelopePic)
        dest?.writeInt(if(fresh) 1 else 0)
        dest?.writeLong(id)
        dest?.writeString(link)
        dest?.writeString(niceDate)
        dest?.writeString(niceShareDate)
        dest?.writeString(origin)
        dest?.writeString(prefix)
        dest?.writeString(projectLink)
        dest?.writeLong(publishTime)
        dest?.writeString(shareData)
        dest?.writeString(shareUser)
        dest?.writeLong(superChapterId)
        dest?.writeString(superChapterName)

        dest?.writeTypedList(tags)

        dest?.writeString(title)
        dest?.writeInt(type)
        dest?.writeLong(userId)
        dest?.writeInt(visible)
        dest?.writeInt(zan)
    }
}