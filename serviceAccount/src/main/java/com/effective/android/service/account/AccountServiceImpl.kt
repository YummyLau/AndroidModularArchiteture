package com.effective.android.service.account

import android.content.Context
import android.util.Log
import com.plugin.component.anno.AutoInjectImpl
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import io.reactivex.Flowable

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectImpl(sdk = [AccountSdk::class])
class AccountServiceImpl : AccountSdk {

    var mApplication: Context? = null

    override fun isLogin(): Boolean {
        return AccessTokenKeeper.readAccessToken(mApplication) != null
    }

    override fun login(accountResult: AccountResult?) {
        AccountComponent.loginResult = accountResult
    }

    override fun logout(accountResult: AccountResult?) {
        AccessTokenKeeper.clear(mApplication)
        accountResult?.onResult(null)
    }

    override fun getAccount(): Flowable<Account> {
        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
                .map { oauth2AccessToken ->
                    Utils.transformAccount(oauth2AccessToken)
                }
    }

}
