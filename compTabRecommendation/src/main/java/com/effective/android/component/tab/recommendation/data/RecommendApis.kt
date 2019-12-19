package com.effective.android.component.tab.recommendation.data

import com.effective.android.component.square.bean.Article
import com.effective.android.service.net.BaseListResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 博文模块api
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */
interface RecommendApis {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @GET("article/list/{pageIndex}/json")
    fun getNewArticles(@Path(value = "pageIndex") index: String): Flowable<BaseListResult<Article>>

    @GET("article/listproject/{pageIndex}/json")
    fun getNewProjects(@Path(value = "pageIndex") index: String): Flowable<BaseListResult<Article>>
}