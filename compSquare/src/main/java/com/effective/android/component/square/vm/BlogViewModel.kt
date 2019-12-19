package com.effective.android.component.square.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.square.data.SquareRepository
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class BlogViewModel : ViewModel() {

    fun getBlogList(pageIndex: Int): Flowable<BaseListResult<IMediaItem>> =
            SquareRepository.get().getBlogList(pageIndex)
                    .compose(RxSchedulers.flowableIoToMain())

}