package com.effective.android.service.account.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effective.android.service.account.UserInfo

@Entity(tableName = "user_info")
class LoginInfoEntity : UserInfo() {

    @PrimaryKey(autoGenerate = true)
    var generateId: Int? = null

    @ColumnInfo(name = "admin")
    override var admin: Boolean = false

    @ColumnInfo(name = "email")
    override var email: String = ""

    @ColumnInfo(name = "icon")
    override var icon: String = ""

    @ColumnInfo(name = "id")
    override var id: Long = 0L

    @ColumnInfo(name = "nickname")
    override var nickname: String = ""

    @ColumnInfo(name = "password")
    override var password: String = ""

    @ColumnInfo(name = "token")
    override val token: String = ""

    @ColumnInfo(name = "type")
    override var type: Int = 0

    @ColumnInfo(name = "username")
    override var username: String = ""
}