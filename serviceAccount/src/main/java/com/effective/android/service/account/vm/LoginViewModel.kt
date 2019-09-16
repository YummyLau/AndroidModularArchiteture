package com.effective.android.service.account.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.Rx2Schedulers
import com.effective.android.service.account.AccountComponent
import com.effective.android.service.account.UserInfo
import io.reactivex.Flowable


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

class LoginViewModel : ViewModel() {

    fun login(username: String, password: String): Flowable<UserInfo> {
        return AccountComponent.accountRepository.login(username, password)
                .compose(Rx2Schedulers.flowableIoToMain())
    }

    fun register(username: String, password: String): Flowable<UserInfo> {
        return AccountComponent.accountRepository.register(username, password)
                .compose(Rx2Schedulers.flowableIoToMain())
    }
}
