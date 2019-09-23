package com.effective.android.component.system.data

import com.effective.android.component.blog.Chapter
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import com.effective.android.component.system.bean.Article
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path


interface SystemApis {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    /**
     * 获取系统列表
     */
    @GET("tree/json")
    fun getTreeChapters(): Flowable<BaseResult<List<Chapter>>>


    /**
     * 知识体系下的文章
     *
     */
    @GET("list/{id}/{pageCount}/json")
    fun getArticles(@Path(value = "id") id: String, @Path(value = "pageCount") pageCount: String): Flowable<BaseListResult<Article>>
}