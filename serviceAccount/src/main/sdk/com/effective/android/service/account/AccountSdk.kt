package com.effective.android.service.account

import io.reactivex.Flowable


interface AccountSdk {

    fun isLogin(): Boolean

    fun logout(): Boolean

    fun login(): Flowable<UserInfo>

}
