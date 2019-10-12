package com.effective.android.component.paccounts.data

import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.bean.Chapter
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PaccountApis {

    companion object {
        const val BASE_URL = "https://wanandroid.com/wxarticle/"
    }

    /**
     * 获取公众号列表
     */
    @GET("chapters/json")
    fun getChapters(): Flowable<BaseResult<List<Chapter>>>


    /**
     * 查看某个公众号历史数据
     *
     */
    @GET("list/{id}/{pageCount}/json")
    fun getArticles(@Path(value = "id") id: String, @Path(value = "pageCount") pageCount: String): Flowable<BaseListResult<Article>>

    /**
     * 在某个公众号中搜索历史文章
     *
     */
    @GET("list/{id}/{pageCount}/json")
    fun getArticlesBySearch(@Path(value = "id") id: String, @Path(value = "pageCount") pageCount: String, @Query(value = "k") key: String): Flowable<BaseListResult<Article>>
}