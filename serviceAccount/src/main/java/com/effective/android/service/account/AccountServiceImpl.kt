package com.effective.android.service.account

import android.content.Context
import android.content.Intent
import com.effective.android.base.rxjava.Rx2Schedulers
import com.effective.android.service.account.view.LoginActivity
import com.plugin.component.anno.AutoInjectImpl
import io.reactivex.Flowable
import io.reactivex.functions.Consumer


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectImpl(sdk = [AccountSdk::class])
class AccountServiceImpl : AccountSdk {

    init {
        AccountComponent.accountServiceImpl = this
    }

    private val listeners = mutableListOf<AccountChangeListener>()

    override fun isLogin(): Boolean = AccountComponent.accountRepository.isLogin()

    override fun logout() {
        val result = AccountComponent.accountRepository.logout()
                .compose(Rx2Schedulers.flowableIoToMain())
                .subscribe(Consumer { })
    }


    override fun login(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun getAccount(): UserInfo? = AccountComponent.accountRepository.userInfo

    override fun addAccountChangeListener(accountChangeListener: AccountChangeListener) {
        if (!listeners.contains(accountChangeListener)) {
            listeners.add(accountChangeListener)
        }
    }

    override fun removeAccountChangeListener(accountChangeListener: AccountChangeListener) {
        if (listeners.contains(accountChangeListener)) {
            listeners.remove(accountChangeListener)
        }
    }

    fun notifyListener(userInfo: UserInfo? = null, login: Boolean, success: Boolean, message: String? = null) {
        for (listener in listeners) {
            listener.onAccountChange(userInfo, login, success, message)
        }
    }
}
