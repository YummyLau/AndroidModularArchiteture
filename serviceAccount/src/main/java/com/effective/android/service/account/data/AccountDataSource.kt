package com.effective.android.service.account.data

import com.effective.android.service.account.UserInfo
import io.reactivex.Flowable


/**
 * account 模块数据接口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

interface AccountDataSource {

    fun isLogin(): Boolean

    fun login(username: String, password: String): Flowable<UserInfo>

    fun register(username: String, password: String): Flowable<UserInfo>

    fun logout():  Flowable<Boolean>

}
