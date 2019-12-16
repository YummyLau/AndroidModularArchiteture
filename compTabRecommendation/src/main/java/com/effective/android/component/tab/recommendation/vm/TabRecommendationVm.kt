package com.effective.android.component.tab.recommendation.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.data.RecommendRepository
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class TabRecommendationVm : ViewModel() {

    fun getRecommendList(pageIndex: Int): Flowable<BaseListResult<Article>> =
            RecommendRepository.get().getRecommendList(pageIndex)
                    .compose(RxSchedulers.flowableIoToMain())

}