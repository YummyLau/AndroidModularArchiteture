package com.effective.android.component.tab.mine.data

import android.content.Context
import com.effective.android.component.tab.mine.Sdks
import com.effective.android.service.account.AccountChangeListener

class MineRepository {

    companion object {
        private var instance: MineRepository? = null
            get() {
                if (field == null) {
                    field = MineRepository()
                }
                return field
            }

        @Synchronized
        fun get(): MineRepository {
            return instance!!
        }
    }

    fun isLogin() = Sdks.serviceAccount.isLogin()

    fun logout() = Sdks.serviceAccount.logout()

    fun login(context: Context) = Sdks.serviceAccount.login(context)

    fun getAccount() = Sdks.serviceAccount.getAccount()

    fun addAccountChangeListener(accountChangeListener: AccountChangeListener) = Sdks.serviceAccount.addAccountChangeListener(accountChangeListener)

    fun removeAccountChangeListener(accountChangeListener: AccountChangeListener) = Sdks.serviceAccount.removeAccountChangeListener(accountChangeListener)
}