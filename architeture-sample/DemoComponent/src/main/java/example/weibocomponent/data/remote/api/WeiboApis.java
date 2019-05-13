package example.weibocomponent.data.remote.api;



import java.util.Map;

import androidx.annotation.Nullable;
import example.weibocomponent.bean.Comment;
import example.weibocomponent.data.local.db.entity.UserEntity;
import example.weibocomponent.data.remote.result.CommentListResult;
import example.weibocomponent.data.remote.result.FriendFollowResult;
import example.weibocomponent.data.remote.result.StatusResult;
import example.weibocomponent.data.remote.result.UrlResult;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/11/24.
 */

public interface WeiboApis {

    String BASE_URL = "https://api.weibo.com/2/";

    /**
     * 用户
     **/
    @GET("users/show.json")
    Flowable<UserEntity> getUser(@Query("access_token") String token, @Query("uid") long id);

    /**
     * 关系
     **/
    //获取用户的关注列表
    @GET("friendships/friends.json")
    Flowable<FriendFollowResult> getFriends(@Query("access_token") String token);

    //获取用户的粉丝列表
    @GET("friendships/followers.json")
    Flowable<FriendFollowResult> getFollowers(@Query("access_token") String token);


    /**
     * 微博
     **/
    //获取所有关注者的微博
    @GET("statuses/home_timeline.json")
    Flowable<StatusResult> getAllStatus(@Query("access_token") String token);

    //获取所有自己的微博
    @GET("statuses/user_timeline.json")
    Flowable<StatusResult> getOwnStatus(@Query("access_token") String token,
                                        @Query("uid") String uid);


    /**
     * 评论
     **/
    //根据微博ID返回某条微博的评论列表
    @GET("comments/show.json")
    Flowable<CommentListResult> getComments(@Query("access_token") String token,
                                            @Query("id") long statusId);

    //获取当前登录用户所发出的评论列表
    @GET("comments/by_me.json")
    Flowable<CommentListResult> getCommentsByMe(@Query("access_token") String token);

    //获取当前登录用户所接收到的评论列表
    @GET("comments/to_me.json")
    Flowable<CommentListResult> getCommentsToMe(@Query("access_token") String token);

    //获取当前登录用户的最新评论包括接收到的与发出的
    @GET("comments/to_me.json")
    Flowable<CommentListResult> getCommentsTimeLine(@Query("access_token") String token);

    //对一条微博进行评论
    @POST("comments/create.json")
    Flowable<Comment> commentsCreate(@Field("access_token") String token,
                                     @Field("comment") String comment,
                                     @Field("id") long statusId);

    //对一条微博进行删除
    @POST("comments/destroy.json")
    Flowable<Comment> commentsDestory(@Field("access_token") String token,
                                      @Field("cid") long commentId);

    //回复一条评论
    @POST("comments/reply.json")
    Flowable<Comment> commentsReply(@Field("access_token") String token,
                                    @Field("cid") long commentId,
                                    @Field("id") long String);

    /**
     * 短链
     */
    //将一个或多个长链接转换成短链接
    @GET("short_url/shorten.json")
    Flowable<UrlResult> urlShorten(@Query("access_token") String token,
                                   @Query("url_long") String urlLong);

    //将一个或多个短链接还原成原始的长链接
    @GET("short_url/expand.json")
    Flowable<UrlResult> urlExpand(@Query("access_token") String token,
                                  @Query("url_short") String urlShort);

    //获取短链接在微博上的微博分享数
    @GET("short_url/share/counts.json")
    Flowable<UrlResult> shortShareCount(@Query("access_token") String token,
                                        @Query("url_short") String urlShort);

    //获取短链接在微博上的微博评论数
    @GET("short_url/comment/counts.json")
    Flowable<UrlResult> shortCommentCount(@Query("access_token") String token,
                                          @Query("url_short") String urlShort);


    /**
     * 公共服务
     **/
    //通过地址编码获取地址名称
    @GET("common/code_to_location.json")
    Flowable<String> codeToLocation(@Query("access_token") String token,
                                    @Query("codes") String codes);

    //获取城市列表
    @GET("common/get_city.json")
    Flowable<String> codeToLocation(@Query("access_token") String token,
                                    @Query("province") String provinc,
                                    @Query("capital") String capital,
                                    @Query("language") String language);

    //获取省份列表
    @GET("common/get_province.json")
    Flowable<String> getProvince(@Query("access_token") String token,
                                 @Query("country") String country,
                                 @Query("capital") String capital,
                                 @Query("language") String language);

    //获取国家列表
    @GET("common/get_country.json")
    Flowable<String> getCountry(@Query("access_token") String token,
                                @Query("capital") String capital,
                                @Query("language") String language);

    //获取时区配置
    @GET("common/get_timezone.json")
    Flowable<Map<Integer, String>> getTimezone(@Query("access_token") String token,
                                               @Nullable @Query("language") String language);
}

