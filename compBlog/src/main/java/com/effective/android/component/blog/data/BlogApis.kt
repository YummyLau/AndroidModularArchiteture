package com.effective.android.component.blog.data

import com.effective.android.component.blog.bean.Banner
import com.effective.android.component.blog.bean.SearchKey
import com.effective.android.component.blog.bean.Website
import com.effective.android.service.kit.data.Article
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
interface BlogApis {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    /**
     * 获取首页文章
     */
    @GET("article/list/{pageIndex}/json")
    fun getArticles(@Path(value = "pageIndex") index: String): Flowable<BaseListResult<Article>>

    /**
     * 获取置顶文章
     */
    @GET("/article/top/json")
    fun getTopArticles(): Flowable<BaseResult<List<Article>>>


    /**
     * 获取首页banner
     */
    @GET("banner/json")
    fun getBanner(): Flowable<BaseResult<List<Banner>>>

    /**
     * 获取常用网站
     */
    @GET("friend/json")
    fun getFriendWebsize(): Flowable<BaseResult<List<Website>>>

    /**
     * 获取搜索热词
     */
    @GET("hotkey/json")
    fun getHotSearchKey(): Flowable<BaseResult<List<SearchKey>>>

}