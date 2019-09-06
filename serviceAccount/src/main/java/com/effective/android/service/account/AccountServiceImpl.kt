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

    override fun getLoginPath(): String = Constants.ROUTER_LOGIN

    override fun getAccount(): Flowable<Account> {
        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
                .map { oauth2AccessToken ->
                    var account: Account? = null
                    if (!oauth2AccessToken.isSessionValid) {
                        Log.e(Constants.LOG_TAG, "AccountServiceImpl#getAccount token invalid!")
                    } else {
                        account = Account()
                        account.uid = Integer.valueOf(oauth2AccessToken.uid).toLong()
                        account.token = oauth2AccessToken.token
                        account.refreshToken = oauth2AccessToken.refreshToken
                        account.expiresTime = oauth2AccessToken.expiresTime
                        account.phoneNum = oauth2AccessToken.phoneNum
                    }
                    account
                }
    }

    override fun logout(forceLogin: Boolean, returnActivityPath: String) {
//        AccessTokenKeeper.clear(mApplication)
//        EventBusUtils.post(AccountEvent(AccountEvent.LOGOUT_TYPE))
//        if (forceLogin) {
//            val postcard = ARouter.getInstance().build(loginPath)
//            if (!TextUtils.isEmpty(returnActivityPath)) {
//                postcard.withString(Constants.RETURN_ACTIVITY_PATH, returnActivityPath)
//            }
//            postcard.navigation()
//        }
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(forceLogin: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun login(returnActivityPath: String) {
//        ARouter.getInstance().build(loginPath)
//                .withString(Constants.RETURN_ACTIVITY_PATH, returnActivityPath)
//                .navigation()
    }

}
