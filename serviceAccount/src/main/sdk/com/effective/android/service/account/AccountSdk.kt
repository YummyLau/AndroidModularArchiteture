package com.effective.android.service.account

import android.content.Context
import io.reactivex.Flowable


interface AccountSdk {

    fun isLogin(): Flowable<Boolean>

    fun logout()

    fun login(context: Context)

    fun getAccount(): Flowable<UserInfo>

    fun addAccountChangeListener(accountChangeListener: AccountChangeListener)

    fun removeAccountChangeListener(accountChangeListener: AccountChangeListener)
}
