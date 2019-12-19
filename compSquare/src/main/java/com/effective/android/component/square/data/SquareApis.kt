package com.effective.android.component.square.data

import com.effective.android.component.square.bean.Banner
import com.effective.android.component.square.bean.BlogArticle
import com.effective.android.component.square.bean.SearchKey
import com.effective.android.component.square.bean.Website
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 博文模块api
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */
interface SquareApis {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }


    /**
     * 获取banner
     */
    @GET("banner/json")
    fun getBanner(): Flowable<BaseResult<List<Banner>>>

    /**
     * 获取置顶文章
     */
    @GET("/article/top/json")
    fun getTopArticles(): Flowable<BaseResult<List<BlogArticle>>>

    /**
     * 获取广场文章
     */
    @GET("user_article/list/{pageIndex}/json")
    fun getArticles(@Path(value = "pageIndex") index: String): Flowable<BaseListResult<BlogArticle>>

}