package com.effective.android.service.account

/**
 * save user account info!
 * You can user jsonString to replace as long as you want
 * Email yummyl.lau@gmail.com
 * wanAndroid 用于信息
 * Created by yummylau on 2018/01/25.
 */

data class UserInfo(var admin: Boolean = false, var email: String = "", var icon: String = "",
                    var id: Long = 0L, var nickname: String = "", var password: String = "", val token: String = "",
                    var type: Int = 0, var username: String = "") {

    fun isValid() = id != 0L

    companion object {
        fun createEmpty(): UserInfo = UserInfo()
    }
}

