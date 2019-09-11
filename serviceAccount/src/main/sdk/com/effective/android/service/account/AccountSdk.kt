package com.effective.android.service.account

import io.reactivex.Flowable

interface AccountSdk {

    fun getAccount(): Flowable<Account>

    fun isLogin(): Boolean

    fun login(accountResult: AccountResult?)

    fun logout(accountResult: AccountResult?)
}
