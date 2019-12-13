package com.effective.android.component.tab.mine.vm

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.tab.mine.R
import com.effective.android.component.tab.mine.Sdks
import com.effective.android.component.tab.mine.data.MineRepository
import com.effective.android.service.account.AccountChangeListener

class MineViewModel : ViewModel() {

    fun logout() = MineRepository.get().logout()

    fun login(context: Context) = MineRepository.get().login(context)

    fun addAccountChangeListener(accountChangeListener: AccountChangeListener) = MineRepository.get().addAccountChangeListener(accountChangeListener)

    fun removeAccountChangeListener(accountChangeListener: AccountChangeListener) = MineRepository.get().removeAccountChangeListener(accountChangeListener)

    fun getLoginAccount() = MineRepository.get().getAccount().compose(RxSchedulers.flowableIoToMain())!!

    fun loadAvatar(imageView: ImageView, avatar: String) {
        Sdks.serviceImageloader.load(imageView, avatar)
    }

    fun loadDefaultAvatar(imageView: ImageView) {
        imageView.setImageResource(R.drawable.mine_ic_logout)
    }

}