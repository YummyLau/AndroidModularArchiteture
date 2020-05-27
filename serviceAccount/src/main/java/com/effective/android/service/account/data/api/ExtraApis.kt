package com.effective.android.service.account.data.api

import com.effective.android.service.account.RankInfo
import com.effective.android.service.account.UserInfo
import com.effective.android.service.account.data.bean.TmpShareBean
import com.effective.android.service.net.BaseResult
import com.effective.android.service.net.ListData

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2020/05/27.
 */

interface ExtraApis {

    //获取rank信息
    @GET("coin/userinfo/json")
    fun getRankInfo(): Flowable<BaseResult<RankInfo>>

    //获取收藏信息
    @GET("collect/list/0/json")
    fun getCollectInfo(): Flowable<BaseResult<ListData<Any>>>

    //获取分享信息
    @GET("private_articles/1/json")
    fun getShareInfo(): Flowable<BaseResult<TmpShareBean>>

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/lg/"
        const val SHARE_URL = "https://www.wanandroid.com/user/lg/"
    }
}

