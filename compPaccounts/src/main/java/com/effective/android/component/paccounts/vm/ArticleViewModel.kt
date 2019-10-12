package com.effective.android.component.paccounts.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.blog.bean.Article
import com.effective.android.component.paccounts.data.PaccountsRepository
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class ArticleViewModel : ViewModel() {

    /**
     * 默认 pageCount 从1开始
     */
    fun getArticles(id: Long, pageCount: Int): Flowable<BaseListResult<Article>> {
        return PaccountsRepository.get().getArticles(id.toString(), pageCount.toString())
                .compose(RxSchedulers.flowableIoToMain())
    }
}