package com.effective.android.component.system.data

import com.effective.android.service.kit.data.Article
import com.effective.android.service.kit.data.Chapter
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


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
    @GET("article/list/{pageCount}/json")
    fun getArticles(@Path(value = "pageCount") pageCount: String,@Query(value = "cid") id: String): Flowable<BaseListResult<Article>>
}