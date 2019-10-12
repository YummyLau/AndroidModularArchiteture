package com.effective.android.component.project.data

import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.bean.Chapter
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 博文模块api
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/09/12.
 */
interface ProjectApis {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/project/"
    }

    /**
     * 获取对应分类下项目列表数据文章
     * 页面从1开始
     */
    @GET("list/{pageIndex}/json")
    fun getArticles(@Path(value = "pageIndex") index: String, @Query(value = "cid") projectId: Long): Flowable<BaseListResult<Article>>


    /**
     * 获取项目数据
     */
    @GET("tree/json")
    fun getProjects(): Flowable<BaseResult<List<Chapter>>>
}