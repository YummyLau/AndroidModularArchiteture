package com.effective.android.service.account.data

import com.effective.android.service.account.UserInfo
import com.effective.android.service.net.BaseResult

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */

interface AccountApis {

    /**
     * {
     * "data": {
     * "admin": false,
     * "chapterTops": [],
     * "collectIds": [],
     * "email": "",
     * "icon": "",
     * "id": 30668,
     * "nickname": "yummylau2",
     * "password": "",
     * "publicName": "yummylau2",
     * "token": "",
     * "type": 0,
     * "username": "yummylau2"
     * },
     * "errorCode": 0,
     * "errorMsg": ""
     * }
     *
     *
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String,
              @Field("password") password: String): Flowable<BaseResult<UserInfo>>

    /**
     * {
     * "data": {
     * "admin": false,
     * "chapterTops": [],
     * "collectIds": [],
     * "email": "",
     * "icon": "",
     * "id": 30668,
     * "nickname": "yummylau2",
     * "password": "",
     * "publicName": "yummylau2",
     * "token": "",
     * "type": 0,
     * "username": "yummylau2"
     * },
     * "errorCode": 0,
     * "errorMsg": ""
     * }
     * 注册
     *
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") repassword: String): Flowable<BaseResult<UserInfo>>

    /**
     * {
     * "data": null,
     * "errorCode": 0,
     * "errorMsg": ""
     * }
     * 退出登陆
     *
     * @return
     */
    @GET("logout/json")
    fun logout(): Flowable<BaseResult<*>>

    companion object {

        const val BASE_URL = "https://www.wanandroid.com/user/"
    }
}

