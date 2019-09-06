package com.effective.android.service.account

/**
 * save user account info!
 * You can user jsonString to replace as long as you want
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class Account {
    var uid: Long = 0
    var token: String? = null
    var refreshToken: String? = null
    var expiresTime: Long = 0
    var phoneNum: String? = null

}
