package com.effective.android.service.account

import io.reactivex.Flowable

interface AccountSdk {

    fun getLoginPath(): String

    fun getAccount(): Flowable<Account>

    fun login(returnActivityPath: String)

    fun logout()

    fun logout(forceLogin: Boolean)

    fun logout(forceLogin: Boolean, returnActivityPath: String)
}
