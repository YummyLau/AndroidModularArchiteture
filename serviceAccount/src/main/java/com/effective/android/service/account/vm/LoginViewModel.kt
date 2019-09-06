package com.effective.android.service.account.vm

import android.util.Log

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import androidx.lifecycle.ViewModel
import com.effective.android.service.account.AccountComponent

import com.effective.android.service.account.Constants.LOG_TAG


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

class LoginViewModel : ViewModel() {

    fun onClickToLogin(ssoHandler: SsoHandler) {
        ssoHandler.authorize(object : WbAuthListener {

            override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
                AccountComponent.accountRepository.saveAccount(oauth2AccessToken)
//                EventBusUtils.post(AccountEvent(AccountEvent.LOGIN_TYPE))
                Log.d(LOG_TAG, "login success!")
            }

            override fun cancel() {
                Log.d(LOG_TAG, "login cancel!")
            }

            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
                Log.d(LOG_TAG, "login fail: " + wbConnectErrorMessage.errorMessage)
            }
        })
    }
}
