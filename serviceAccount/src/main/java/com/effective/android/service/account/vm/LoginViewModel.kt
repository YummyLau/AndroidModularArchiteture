package com.effective.android.service.account.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.service.account.UserInfo
import com.effective.android.service.account.data.AccountRepository
import io.reactivex.Flowable


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

class LoginViewModel : ViewModel() {

    fun login(username: String, password: String): Flowable<UserInfo> {
        return AccountRepository.get().login(username, password)
                .compose(RxSchedulers.flowableIoToMain())
    }

    fun register(username: String, password: String): Flowable<UserInfo> {
        return AccountRepository.get().register(username, password)
                .compose(RxSchedulers.flowableIoToMain())
    }
}
