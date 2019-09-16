package com.effective.android.service.account.data

import android.app.Application
import android.text.TextUtils
import com.effective.android.base.rxjava.Rx2Creator
import com.effective.android.base.util.GsonUtils
import com.effective.android.service.account.AccountComponent
import com.effective.android.service.account.UserInfo
import com.effective.android.service.net.Type
import io.reactivex.Flowable
import java.util.concurrent.Callable


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class AccountRepository(private val application: Application) : AccountDataSource {

    val USER_File: String = "user_file"
    val USER_KEY: String = "user_key"
    var userInfo: UserInfo? = null
    var accountApis: AccountApis? = null

    init {
        var userString = application.getSharedPreferences(USER_File, android.content.Context.MODE_PRIVATE).getString(USER_KEY, "")
        if (!TextUtils.isEmpty(userString)) {
            userInfo = GsonUtils.getObj(userString, UserInfo::class.java)
        }
    }

    override fun isLogin(): Boolean = userInfo != null

    override fun logout(): Flowable<Boolean> {
        return getAccountApi().logout()
                .flatMap {
                    Rx2Creator.createFlowable(Callable<Boolean> {
                        AccountComponent.accountServiceImpl.notifyListener(userInfo, false, success = it.isSuccess, message = it.errorMsg)
                        it.isSuccess
                    })
                }
    }

    override fun login(username: String, password: String): Flowable<UserInfo> {
        return getAccountApi().login(username, password)
                .flatMap {
                    Rx2Creator.createFlowable(Callable<UserInfo> {
                        val data = if (it.isSuccess) it.data else UserInfo.createEmpty()
                        AccountComponent.accountServiceImpl.notifyListener(data, true, success = it.isSuccess, message = it.errorMsg)
                        data
                    })
                }
    }

    override fun register(username: String, password: String): Flowable<UserInfo> {
        return getAccountApi().register(username, password, password)
                .flatMap {
                    Rx2Creator.createFlowable(Callable<UserInfo> {
                        val data = if (it.isSuccess) it.data else UserInfo.createEmpty()
                        AccountComponent.accountServiceImpl.notifyListener(data, true, success = it.isSuccess, message = it.errorMsg)
                        data
                    })
                }
    }

    private fun getAccountApi(): AccountApis {
        if (accountApis == null) {
            accountApis = AccountComponent.serviceNet.service(
                    AccountApis.BASE_URL, Type.GSON, AccountApis::class.java)
        }
        return accountApis!!
    }
}
