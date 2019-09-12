package com.effective.android.service.account.vm

import androidx.lifecycle.ViewModel
import com.effective.android.service.account.AccountComponent

import com.effective.android.service.account.Utils


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

class LoginViewModel : ViewModel() {

//    fun onClickToLogin(ssoHandler: SsoHandler, accountResult: AccountResult) {
//        ssoHandler.authorize(object : WbAuthListener {
//
//            override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
//                AccountComponent.accountRepository.saveAccount(oauth2AccessToken)
//                accountResult.onResult(Utils.transformAccount(oauth2AccessToken))
//            }
//
//            override fun cancel() {
//                accountResult.onResult(null)
//            }
//
//            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
//                accountResult.onResult(null)
//            }
//        })
//    }
}
