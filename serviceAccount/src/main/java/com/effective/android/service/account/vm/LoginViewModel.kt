package com.effective.android.service.account.vm

import android.util.Log

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import androidx.lifecycle.ViewModel
import com.effective.android.service.account.AccountComponent
import com.effective.android.service.account.AccountResult

import com.effective.android.service.account.Constants.LOG_TAG
import com.effective.android.service.account.Utils


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

class LoginViewModel : ViewModel() {

    fun onClickToLogin(ssoHandler: SsoHandler, accountResult: AccountResult) {
        ssoHandler.authorize(object : WbAuthListener {

            override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
                AccountComponent.accountRepository.saveAccount(oauth2AccessToken)
                accountResult.onResult(Utils.transformAccount(oauth2AccessToken))
            }

            override fun cancel() {
                accountResult.onResult(null)
            }

            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
                accountResult.onResult(null)
            }
        })
    }
}
