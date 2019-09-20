package com.effective.android.component.project.data

import com.effective.android.component.project.Sdks
import com.effective.android.component.project.bean.Article
import com.effective.android.component.project.bean.Project
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import com.effective.android.service.net.Type
import io.reactivex.Flowable


/**
 * 单例仓库类管理
 * created by yummylau on 2019/09/20
 */
class ProjectRepository private constructor() {

    private val projectApis by lazy {
        Sdks.serviceNet.service(
                ProjectApis.BASE_URL, Type.GSON, ProjectApis::class.java)
    }

    companion object {
        private var instance: ProjectRepository? = null
            get() {
                if (field == null) {
                    field = ProjectRepository()
                }
                return field
            }

        @Synchronized
        fun get(): ProjectRepository {
            return instance!!
        }
    }

    fun getArticles(index: String, projectId: Long): Flowable<BaseListResult<Article>> = projectApis.getArticles(index, projectId)

    fun getProjects(): Flowable<BaseResult<List<Project>>> = projectApis.getProjects()

}