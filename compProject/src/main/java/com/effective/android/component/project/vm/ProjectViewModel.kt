package com.effective.android.component.project.vm

import androidx.lifecycle.ViewModel
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.component.project.data.ProjectRepository
import com.effective.android.component.square.bean.Chapter
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable

class ProjectViewModel : ViewModel() {

    fun getProjects(): Flowable<BaseResult<List<Chapter>>> {
        return ProjectRepository.get().getProjects()
                .compose(RxSchedulers.flowableIoToMain())
    }

}