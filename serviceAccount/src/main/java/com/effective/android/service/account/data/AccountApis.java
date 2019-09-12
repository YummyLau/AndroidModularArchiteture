package com.effective.android.service.account.data;

import com.effective.android.service.account.Account;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */

public interface AccountApis {

    String BASE_URL = "https://www.wanandroid.com/user/";

    @POST("login")
    Flowable<Account> login(@Field("username") String username,
                            @Field("password") String password);

    @POST("register")
    Flowable<Account> register(@Field("username") String username,
                               @Field("password") String password,
                               @Field("repassword") String repassword);
    @GET("logout/json")
    Flowable<Boolean> logout();
}

