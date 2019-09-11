package com.effective.android.service.account

import android.util.Log
import com.sina.weibo.sdk.auth.Oauth2AccessToken

internal object Utils {

    @JvmStatic
    fun transformAccount(oauth2AccessToken: Oauth2AccessToken?): Account? {
        if (oauth2AccessToken == null || !oauth2AccessToken?.isSessionValid) {
            Log.e(Constants.LOG_TAG, "AccountServiceImpl#getAccount token invalid!")
            return null
        }
        var account = Account()
        account.uid = Integer.valueOf(oauth2AccessToken.uid).toLong()
        account.token = oauth2AccessToken.token
        account.refreshToken = oauth2AccessToken.refreshToken
        account.expiresTime = oauth2AccessToken.expiresTime
        account.phoneNum = oauth2AccessToken.phoneNum
        return account
    }

}