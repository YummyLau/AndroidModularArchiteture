package com.effective.android.service.account

import android.app.Application

import com.effective.android.service.account.data.AccountRepository
import com.plugin.component.IComponent
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class AccountComponent : IComponent {

    companion object {
        lateinit var accountRepository: AccountRepository
        var loginResult: AccountResult? = null

        fun syncLoginResult(account: Account?) {
            loginResult?.onResult(account)
        }
    }

    override fun attachComponent(application: Application) {
        WbSdk.install(application, AuthInfo(application, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE))
        accountRepository = AccountRepository(application)
    }

    override fun detachComponent() {
        loginResult = null

    }

}
