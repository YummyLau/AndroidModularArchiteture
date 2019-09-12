package com.effective.android.service.account

import com.plugin.component.anno.AutoInjectImpl
import io.reactivex.Flowable


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectImpl(sdk = [AccountSdk::class])
class AccountServiceImpl : AccountSdk {


    override fun isLogin(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(): Flowable<UserInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //
//    override fun isLogin(): Boolean {
//        return AccessTokenKeeper.readAccessToken(mApplication) != null
//    }
//
//    override fun login(context: Context, accountResult: AccountResult?) {
//        AccountComponent.loginResult = accountResult
//        context.startActivity(Intent("com.effective.android.service.account.view.LoginActivity"))
//    }
//
//    override fun logout(accountResult: AccountResult?) {
//        AccessTokenKeeper.clear(mApplication)
//        accountResult?.onResult(null)
//    }
//
//    override fun getAccount(): Flowable<UserInfo> {
//        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
//                .map { oauth2AccessToken ->
//                    Utils.transformAccount(oauth2AccessToken)
//                }
//    }


}
