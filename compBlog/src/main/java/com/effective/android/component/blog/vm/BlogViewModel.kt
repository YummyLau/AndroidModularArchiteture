package com.effective.android.component.blog.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.data.BlogRepository
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class BlogViewModel : ViewModel() {

    fun getBlogList(pageIndex: Int): Flowable<BaseListResult<IMediaItem>> =
            BlogRepository.get().getBlogList(pageIndex)
                    .compose(RxSchedulers.flowableIoToMain())

}