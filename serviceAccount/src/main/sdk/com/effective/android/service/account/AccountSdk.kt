package com.effective.android.service.account

import android.content.Context


interface AccountSdk {

    fun isLogin(): Boolean

    fun logout()

    fun login(context: Context)

    fun getAccount(): UserInfo?

    fun addAccountChangeListener(accountChangeListener: AccountChangeListener)

    fun removeAccountChangeListener(accountChangeListener: AccountChangeListener)
}
