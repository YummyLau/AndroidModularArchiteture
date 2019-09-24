package com.effective.android.service.account

interface AccountChangeListener {

    fun onAccountChange(userInfo: UserInfo?, login: Boolean, success: Boolean, message: String?)
}
