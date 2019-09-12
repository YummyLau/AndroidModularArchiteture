package com.effective.android.service.account.data


/**
 * account 模块数据接口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

interface AccountDataSource {

    fun isLogin(): Boolean

    fun login(username: String, password: String)

    fun login(): Boolean

}
