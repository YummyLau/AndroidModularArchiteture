package com.effective.android.service.account.data

import android.app.Application
import android.text.TextUtils

import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener


import com.effective.android.service.account.Constants


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class AccountRepository(private val mApplication: Application) : AccountDataSource {


//    override fun saveAccount(oauth2AccessToken: Oauth2AccessToken) {
//        if (oauth2AccessToken.isSessionValid) {
//            AccessTokenKeeper.writeAccessToken(mApplication, oauth2AccessToken)
//        }
//    }
}
