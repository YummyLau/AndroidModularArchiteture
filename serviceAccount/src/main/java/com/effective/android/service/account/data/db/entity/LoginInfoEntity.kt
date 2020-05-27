package com.effective.android.service.account.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effective.android.base.util.GsonUtils
import com.effective.android.service.account.RankInfo
import com.effective.android.service.account.UserInfo

@Entity(tableName = "login_info")
class LoginInfoEntity {


    companion object {

        @JvmStatic
        fun fromUserInfo(userInfo: UserInfo?): LoginInfoEntity {
            val loginInfoEntity = LoginInfoEntity()
            if(userInfo != null){
                loginInfoEntity.admin = userInfo.admin
                loginInfoEntity.email = userInfo.email
                loginInfoEntity.icon = userInfo.icon
                loginInfoEntity.id = userInfo.id
                loginInfoEntity.nickname = userInfo.nickname
                loginInfoEntity.password = userInfo.password
                loginInfoEntity.token = userInfo.token
                loginInfoEntity.type = userInfo.type
                loginInfoEntity.username = userInfo.username
                loginInfoEntity.updateTime = System.currentTimeMillis()
                loginInfoEntity.rankInfo = GsonUtils.getJsonString(userInfo.rankInfo) ?: ""
            }
            return loginInfoEntity
        }
    }


    fun toUserInfo(): UserInfo {
        val userInfo = UserInfo()
        userInfo.admin = admin
        userInfo.email = email
        userInfo.icon = icon
        userInfo.id = id
        userInfo.nickname = nickname
        userInfo.password = password
        userInfo.token = token
        userInfo.type = type
        userInfo.username = username
        userInfo.rankInfo = GsonUtils.getObj(rankInfo, RankInfo::class.java) ?: RankInfo.createEmpty()
        return userInfo
    }

    /**
     * 0 表示未登录
     * 1 表示已经登录
     */
    @ColumnInfo(name = "loginState")
    var loginState: Int = 0

    /**
     * 更新时间
     */
    @ColumnInfo(name = "updateTime")
    var updateTime:Long = 0L

    @ColumnInfo(name = "admin")
    var admin: Boolean = false

    @ColumnInfo(name = "email")
    var email: String = ""

    @ColumnInfo(name = "icon")
    var icon: String = ""

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0L

    @ColumnInfo(name = "nickname")
    var nickname: String = ""

    @ColumnInfo(name = "password")
    var password: String = ""

    @ColumnInfo(name = "token")
    var token: String = ""

    @ColumnInfo(name = "type")
    var type: Int = 0

    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "rankInfo")
    var rankInfo: String = ""

    fun isLogin() = loginState == 1
}