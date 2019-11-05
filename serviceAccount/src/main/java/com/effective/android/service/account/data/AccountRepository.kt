package com.effective.android.service.account.data

import android.text.TextUtils
import com.effective.android.base.rxjava.RxCreator
import com.effective.android.base.util.GsonUtils
import com.effective.android.service.account.AccountChangeListener
import com.effective.android.service.account.AccountComponent
import com.effective.android.service.account.Sdks
import com.effective.android.service.account.UserInfo
import com.effective.android.service.net.Type
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class AccountRepository() : AccountDataSource {

    val USER_File: String = "user_file"
    val USER_KEY: String = "user_key"
    var userInfo: UserInfo? = null
    val listeners = mutableListOf<AccountChangeListener>()

    companion object {
        private var instance: AccountRepository? = null
            get() {
                if (field == null) {
                    field = AccountRepository()
                }
                return field
            }

        @Synchronized
        fun get(): AccountRepository {
            return instance!!
        }
    }

    private val accountApis by lazy {
        Sdks.serviceNet.service(
                AccountApis.BASE_URL, Type.GSON, AccountApis::class.java)
    }

    init {
        var userString = AccountComponent.sApplication.getSharedPreferences(USER_File, android.content.Context.MODE_PRIVATE).getString(USER_KEY, "")
        if (!TextUtils.isEmpty(userString)) {
            userInfo = GsonUtils.getObj(userString, UserInfo::class.java)
        }
    }
    

    override fun isLogin(): Boolean = userInfo != null

    override fun logout(): Flowable<Boolean> {
        return accountApis.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    RxCreator.createFlowable(Callable<Boolean> {
                        notifyAccountChange(userInfo, false, success = it.isSuccess, message = it.errorMsg)
                        it.isSuccess
                    }).subscribeOn(AndroidSchedulers.mainThread())
                }
    }

    override fun login(username: String, password: String): Flowable<UserInfo> {
        return accountApis.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    RxCreator.createFlowable(Callable<UserInfo> {
                        val data = if (it.isSuccess) it.data else UserInfo.createEmpty()
                        notifyAccountChange(data, true, success = it.isSuccess, message = it.errorMsg)
                        data
                    }).subscribeOn(AndroidSchedulers.mainThread())
                }
    }

    override fun register(username: String, password: String): Flowable<UserInfo> {
        return accountApis.register(username, password, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    RxCreator.createFlowable(Callable<UserInfo> {
                        val data = if (it.isSuccess) it.data else UserInfo.createEmpty()
                        notifyAccountChange(data, true, success = it.isSuccess, message = it.errorMsg)
                        data
                    }).subscribeOn(AndroidSchedulers.mainThread())
                }
    }

    private fun notifyAccountChange(userInfo: UserInfo?, login: Boolean, success: Boolean, message: String?) {
        for (listener in listeners) {
            listener.onAccountChange(userInfo, login, success, message)
        }
    }
}
