package com.effective.android.component.project.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.project.data.ProjectRepository
import com.effective.android.service.kit.data.Article
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable

class ArticleViewModel : ViewModel() {


    fun getArticles(index: Int, projectId: Long): Flowable<BaseListResult<Article>> {
        return ProjectRepository.get().getArticles(index.toString(), projectId)
                .compose(RxSchedulers.flowableIoToMain())
    }
}