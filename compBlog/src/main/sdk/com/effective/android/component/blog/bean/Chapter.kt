package com.effective.android.component.blog.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Chapter : Parcelable {

    @SerializedName("children")
    var children: MutableList<Chapter> = mutableListOf()

    @SerializedName("courseId")
    var courseId: Int = -1

    @SerializedName("id")
    var id: Long = -1

    @SerializedName("name")
    var name: String = ""

    @SerializedName("order")
    var order: Int = -1

    @SerializedName("parentChapterId")
    var parentChapterId: Long = -1

    @SerializedName("userControlSetTop")
    var userControlSetTop: Boolean = false

    @SerializedName("visible")
    var visible: Int = -1

    constructor()

    constructor(source: Parcel) {
//        children = source.readString()

            val array: ArrayList<Chapter> = ArrayList()
            source.readTypedList(array, CREATOR)
            children = array.toMutableList()

        courseId = source.readInt()
        id = source.readLong()
        name = source.readString()
        order = source.readInt()
        parentChapterId = source.readLong()
        userControlSetTop = source.readInt() != 0
        visible = source.readInt()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Chapter> = object : Parcelable.Creator<Chapter> {
            override fun createFromParcel(source: Parcel): Chapter {
                return Chapter(source)
            }

            override fun newArray(size: Int): Array<Chapter> {
                return Array(size) { Chapter() }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeTypedList(children)
        dest?.writeInt(courseId)
        dest?.writeLong(id)
        dest?.writeString(name)
        dest?.writeInt(order)
        dest?.writeLong(parentChapterId)
        dest?.writeInt(if (userControlSetTop) 1 else 0)
        dest?.writeInt(visible)
    }
}