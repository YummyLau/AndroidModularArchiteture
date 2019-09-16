package com.effective.android.service.account.data;

import com.effective.android.service.account.UserInfo;
import com.effective.android.service.net.BaseResult;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */

public interface AccountApis {

    String BASE_URL = "https://www.wanandroid.com/user/";

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
     * <p>
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Flowable<BaseResult<UserInfo>> login(@Field("username") String username,
                                         @Field("password") String password);

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
    Flowable<BaseResult<UserInfo>> register(@Field("username") String username,
                                            @Field("password") String password,
                                            @Field("repassword") String repassword);

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
    Flowable<BaseResult> logout();
}

