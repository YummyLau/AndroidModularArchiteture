package com.effective.android.component.paccounts.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.square.bean.Chapter
import com.effective.android.component.paccounts.data.PaccountsRepository
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable

class PaccountsViewModel  : ViewModel(){

    fun getPaccounts(): Flowable<BaseResult<List<Chapter>>> {
        return PaccountsRepository.get().getChapters()
                .compose(RxSchedulers.flowableIoToMain())
    }
}