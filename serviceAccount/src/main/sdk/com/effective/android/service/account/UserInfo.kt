package com.effective.android.service.account

/**
 * save user account info!
 * You can user jsonString to replace as long as you want
 * Email yummyl.lau@gmail.com
 * wanAndroid 用于信息
 * Created by yummylau on 2018/01/25.
 */

open class UserInfo {

    open var admin: Boolean = false
    open var email: String = ""
    open var icon: String = ""
    open var id: Long = 0L
    open var nickname: String = ""
    open var password: String = ""
    open var token: String = ""
    open var type: Int = 0
    open var username: String = ""
    open var rankInfo: RankInfo = RankInfo.createEmpty()
    open var actionInfo:ActionInfo = ActionInfo.createEmpty()

    fun isValid() = id != 0L

    companion object {
        fun createEmpty(): UserInfo = UserInfo()
    }
}

