package com.effective.android.service.account

import android.content.Context
import io.reactivex.Flowable


interface AccountSdk {

    fun isLogin(): Boolean

    fun logout(): Flowable<Boolean>

    fun login(context: Context)

    fun getAccount(): UserInfo?

    fun addAccountChangeListener(accountChangeListener: AccountChangeListener)

    fun removeAccountChangeListener(accountChangeListener: AccountChangeListener)
}
