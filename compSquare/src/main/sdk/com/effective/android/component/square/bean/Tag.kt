package com.effective.android.component.square.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * 文章tag实体
 * created by yummylau on 2019/09/18
 */
open class Tag : Parcelable{

    @SerializedName("name")
    var name:String? = null

    @SerializedName("url")
    var url:String? = null

    constructor()

    constructor(source: Parcel) {
        name = source.readString()
        url = source.readString()
    }

    companion object{

        @JvmField
        val CREATOR: Parcelable.Creator<Tag> = object : Parcelable.Creator<Tag> {
            override fun createFromParcel(source: Parcel): Tag {
                return Tag(source)
            }

            override fun newArray(size: Int): Array<Tag> {
                return Array(size) { Tag() }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(url)
    }

}