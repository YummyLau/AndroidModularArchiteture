package com.effective.android.service.account.data

import androidx.annotation.WorkerThread
import com.effective.android.base.rxjava.RxCreator
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.service.account.AccountChangeListener
import com.effective.android.service.account.Sdks
import com.effective.android.service.account.UserInfo
import com.effective.android.service.account.data.db.AccountDataBase
import com.effective.android.service.account.data.db.entity.LoginInfoEntity
import com.effective.android.service.net.Type
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class AccountRepository() {

    val listeners = mutableListOf<AccountChangeListener>()
    var user: LoginInfoEntity? = null

    private var users: MutableSet<LoginInfoEntity>? = null
    private val accountApis by lazy {
        Sdks.serviceNet.service(
                AccountApis.BASE_URL, Type.GSON, AccountApis::class.java)
    }

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

    @WorkerThread
    private fun getDataFromDB(): Boolean {
        if (users == null) {
            users = mutableSetOf()
            val data = AccountDataBase.instance.getLoginDao().getAll()
            for (user in data) {
                if (user.isLogin()) {
                    this.user = user
                }
                users!!.add(user)
            }
            return true
        }
        return false
    }

    fun getLoginHistory(): MutableList<LoginInfoEntity>? = users?.toMutableList()

    @WorkerThread
    private fun updateDataToDB(loginInfoEntity: LoginInfoEntity?, boolean: Boolean) {
        if (loginInfoEntity == null) {
            return
        }
        //入库
        loginInfoEntity?.updateTime = System.currentTimeMillis()
        loginInfoEntity?.loginState = if (boolean) 1 else 0
        AccountDataBase.instance.getLoginDao().insert(loginInfoEntity!!)
        //更新缓存
        users?.add(loginInfoEntity)
        user = if (boolean) null else loginInfoEntity
    }


    fun getUserInfo(): Flowable<UserInfo> {
        if (user != null) {
            return RxCreator.createFlowable (Callable<UserInfo> { user!!.toUserInfo() })
        }
        return RxCreator
                .createFlowable(Callable<Boolean> { getDataFromDB() })
                .map {
                    if (user != null) {
                        user!!.toUserInfo()
                    } else {
                        UserInfo.createEmpty()
                    }
                }
    }

    fun isLogin(): Flowable<Boolean> {
        return RxCreator
                .createFlowable(Callable<Boolean> { getDataFromDB() })
                .map {
                    user != null
                }
    }

    fun logout(): Flowable<Boolean> {
        return RxCreator
                .createFlowable(Callable<Boolean> { getDataFromDB() })
                .flatMap { accountApis.logout() }
                .map {
                    if (it.isSuccess) {
                        updateDataToDB(user, false)
                    }
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    notifyAccountChange(user?.toUserInfo(), false, success = it.isSuccess, message = it.errorMsg)
                    it.isSuccess
                }
    }

    fun login(username: String, password: String): Flowable<UserInfo> {
        return RxCreator
                .createFlowable(Callable<Boolean> { getDataFromDB() })
                .flatMap { accountApis.login(username, password) }
                .map {
                    if (it.isSuccess) {
                        it.data?.password = password
                        user = LoginInfoEntity.fromUserInfo(it.data)
                        updateDataToDB(user, true)
                    }
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val data = if (it.isSuccess) {
                        it.data
                    } else {
                        UserInfo.createEmpty()
                    }
                    notifyAccountChange(data, true, success = it.isSuccess, message = it.errorMsg)
                    data
                }
    }

    fun register(username: String, password: String): Flowable<UserInfo> {
        return RxCreator
                .createFlowable(Callable<Boolean> { getDataFromDB() })
                .flatMap { accountApis.register(username, password, password) }
                .map {
                    if (it.isSuccess) {
                        it.data?.password = password
                        user = LoginInfoEntity.fromUserInfo(it.data)
                        updateDataToDB(user, true)
                    }
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val data = if (it.isSuccess) {
                        it.data
                    } else {
                        UserInfo.createEmpty()
                    }
                    notifyAccountChange(data, true, success = it.isSuccess, message = it.errorMsg)
                    data
                }
    }

    private fun notifyAccountChange(userInfo: UserInfo?, login: Boolean, success: Boolean, message: String?) {
        for (listener in listeners) {
            listener.onAccountChange(userInfo, login, success, message)
        }
    }
}
