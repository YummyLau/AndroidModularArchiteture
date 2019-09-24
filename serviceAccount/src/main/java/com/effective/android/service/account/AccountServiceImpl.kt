package com.effective.android.service.account

import android.content.Context
import android.content.Intent
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.service.account.data.AccountRepository
import com.effective.android.service.account.view.LoginActivity
import com.plugin.component.anno.AutoInjectImpl
import io.reactivex.functions.Consumer


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectImpl(sdk = [AccountSdk::class])
class AccountServiceImpl : AccountSdk {



    override fun isLogin(): Boolean = AccountRepository.get().isLogin()

    override fun logout() {
        val result = AccountRepository.get().logout()
                .compose(RxSchedulers.flowableIoToMain())
                .subscribe(Consumer { })
    }


    override fun login(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun getAccount(): UserInfo? = AccountRepository.get().userInfo

    override fun addAccountChangeListener(accountChangeListener: AccountChangeListener) {
        if (!AccountRepository.get().listeners.contains(accountChangeListener)) {
            AccountRepository.get().listeners.add(accountChangeListener)
        }
    }

    override fun removeAccountChangeListener(accountChangeListener: AccountChangeListener) {
        if (AccountRepository.get().listeners.contains(accountChangeListener)) {
            AccountRepository.get().listeners.remove(accountChangeListener)
        }
    }
}
