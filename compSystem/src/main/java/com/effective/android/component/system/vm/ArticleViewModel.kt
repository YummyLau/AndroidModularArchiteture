package com.effective.android.component.system.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.square.bean.Article
import com.effective.android.component.system.data.SystemRepository
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class ArticleViewModel : ViewModel() {

    /**
     * 默认 pageCount 从1开始
     */
    fun getArticles(id: Long, pageCount: Int): Flowable<BaseListResult<Article>> {
        return SystemRepository.get().getArticles(pageCount.toString(), id.toString())
                .compose(RxSchedulers.flowableIoToMain())
    }
}