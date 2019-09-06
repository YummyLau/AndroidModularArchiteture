package com.effective.android.component.weibo.data.local.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Entity(tableName = UserEntity.TABLE_NAME,
        primaryKeys = UserEntity.COLUMN_ID)
public class UserEntity {

    public static final String TABLE_NAME = "user_table";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_IDSTR = "user_idstr";
    public static final String COLUMN_SCREEN = "user_screen";
    public static final String COLUMN_NAME = "user_name";
    public static final String COLUMN_PROVINCE = "user_province";
    public static final String COLUMN_CITY = "user_city";
    public static final String COLUMN_LOCATION = "user_location";
    public static final String COLUMN_DESCRIPTION = "user_description";
    public static final String COLUMN_URL = "user_url";
    public static final String COLUMN_IMAGE_URL = "user_image_url";
    public static final String COLUMN_PROFILE_URL = "user_profile_url";
    public static final String COLUMN_DOMAIN = "user_domain";
    public static final String COLUMN_WEIBAO = "user_weibao";
    public static final String COLUMN_GENDER = "user_gender";
    public static final String COLUMN_FOLLOWERS_COUNT = "user_followers_count";
    public static final String COLUMN_FRIENDS_COUNT = "user_friends_count";
    public static final String COLUMN_STATUSES_COUNT = "user_statuses_count";
    public static final String COLUMN_FAVOURITES_COUNT = "user_favourites_count";
    public static final String COLUMN_CREATED_AT = "user_created_at";
    public static final String COLUMN_ALLOW_ALL_ACT_MSG = "user_allow_all_act_msg";
    public static final String COLUMN_GEO_ENABLE = "user_geo_enable";
    public static final String COLUMN_VERIFIED = "user_verified";
    public static final String COLUMN_REMARK = "user_remark";
    public static final String COLUMN_ALLOW_ALL_COMMENT = "user_allow_all_comment";
    public static final String COLUMN_AVATAR_LARGE = "user_avatar_large";
    public static final String COLUMN_AVTAR_HD = "user_avatar_hd";
    public static final String COLUMN_VERIFIED_REASON = "user_verified_reason";
    public static final String COLUMN_FOLLOW_ME = "user_follow_me";
    public static final String COLUMN_ONLINE_STATUS = "user_online_status";
    public static final String COLUMN_BI_FOLLOWERS_COUNT = "user_bi_followers_count";
    public static final String COLUMN_LANG = "user_lang";

    @Ignore
    private boolean emptyObj = false;

    public static final UserEntity EMPTY_OBJ = new UserEntity(true);

    public boolean isEmptyObj() {
        return emptyObj;
    }

    public UserEntity(boolean emptyObj) {
        this.emptyObj = emptyObj;
    }

    public UserEntity() {
    }

    //用户UID
    @ColumnInfo(index = true, name = COLUMN_ID)
    @SerializedName("id")
    public long id;

    //字符串型的用户UID
    @ColumnInfo(name = COLUMN_IDSTR)
    @SerializedName("idStr")
    public String idStr;

    //用户昵称
    @ColumnInfo(name = COLUMN_SCREEN)
    @SerializedName("screen")
    public String nick;

    //友好显示名称
    @ColumnInfo(name = COLUMN_NAME)
    @SerializedName("name")
    public String name;

    //用户所在省级ID
    @ColumnInfo(name = COLUMN_PROVINCE)
    @SerializedName("province")
    public int province;

    //用户所在城市ID
    @ColumnInfo(name = COLUMN_CITY)
    @SerializedName("city")
    public int city;

    //用户所在地
    @ColumnInfo(name = COLUMN_LOCATION)
    @SerializedName("location")
    public String location;

    //用户个人描述
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    @SerializedName("description")
    public String description;

    //用户博客地址
    @ColumnInfo(name = COLUMN_URL)
    @SerializedName("url")
    public String url;

    //用户头像地址（中图），50×50像素
    @ColumnInfo(name = COLUMN_IMAGE_URL)
    @SerializedName("profile_image_url")
    public String profileImageUrl;

    //用户的微博统一URL地址
    @ColumnInfo(name = COLUMN_PROFILE_URL)
    @SerializedName("profile_url")
    public String profile_url;

    //用户的个性化域名
    @ColumnInfo(name = COLUMN_DOMAIN)
    @SerializedName("domain")
    public String domain;

    //用户的微号
    @ColumnInfo(name = COLUMN_WEIBAO)
    @SerializedName("weihao")
    public String weihao;

    //性别，m：男、f：女、n：未知
    @ColumnInfo(name = COLUMN_GENDER)
    @SerializedName("gender")
    public String gender;

    //粉丝数
    @ColumnInfo(name = COLUMN_FOLLOWERS_COUNT)
    @SerializedName("followers_count")
    public int followersCount;

    //关注数
    @ColumnInfo(name = COLUMN_FRIENDS_COUNT)
    @SerializedName("friends_count")
    public int friendsCount;

    //微博数
    @ColumnInfo(name = COLUMN_STATUSES_COUNT)
    @SerializedName("statuses_count")
    public int statusesCount;

    //收藏数
    @ColumnInfo(name = COLUMN_FAVOURITES_COUNT)
    @SerializedName("favourites_count")
    public int favouritesCount;

    //用户创建（注册）时间
    @ColumnInfo(name = COLUMN_CREATED_AT)
    @SerializedName("created_at")
    public String createdAt;

    //是否允许所有人给我发私信，true：是，false：否
    @ColumnInfo(name = COLUMN_ALLOW_ALL_ACT_MSG)
    @SerializedName("allow_all_act_msg")
    public boolean allowAllActMsg;

    //是否允许标识用户的地理位置，true：是，false：否
    @ColumnInfo(name = COLUMN_GEO_ENABLE)
    @SerializedName("geo_enabled")
    public boolean geoEnabled;

    //是否是微博认证用户，即加V用户，true：是，false：否
    @ColumnInfo(name = COLUMN_VERIFIED)
    @SerializedName("verified")
    public boolean verified;

    //用户备注信息，只有在查询用户关系时才返回此字段
    @ColumnInfo(name = COLUMN_REMARK)
    @SerializedName("remark")
    public String remark;

    //是否允许所有人对我的微博进行评论，true：是，false：否
    @ColumnInfo(name = COLUMN_ALLOW_ALL_COMMENT)
    @SerializedName("allow_all_comment")
    public boolean allowAllComment;

    //用户头像地址（大图），180×180像素
    @ColumnInfo(name = COLUMN_AVATAR_LARGE)
    @SerializedName("avatar_large")
    public String avatarLarge;

    //用户头像地址（高清），高清头像原图
    @ColumnInfo(name = COLUMN_AVTAR_HD)
    @SerializedName("avatar_hd")
    public String avatarHd;

    //认证原因
    @ColumnInfo(name = COLUMN_VERIFIED_REASON)
    @SerializedName("verified_reason")
    public String verifiedReason;

    //该用户是否关注当前登录用户，true：是，false：否
    @ColumnInfo(name = COLUMN_FOLLOW_ME)
    @SerializedName("follow_me")
    public boolean followMe;

    //用户的在线状态，0：不在线、1：在线
    @ColumnInfo(name = COLUMN_ONLINE_STATUS)
    @SerializedName("online_status")
    public int onlineStatus;

    //用户的互粉数
    @ColumnInfo(name = COLUMN_BI_FOLLOWERS_COUNT)
    @SerializedName("bi_followers_count")
    public int biFollowersCount;

    //用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
    @ColumnInfo(name = COLUMN_LANG)
    @SerializedName("lang")
    public String lang;

    //暂未支持
    @Ignore
    @SerializedName("following")
    public boolean following;

    //暂未支持
    @Ignore
    @SerializedName("verified_type")
    public int verified_type;

    //用户的最近一条微博信息字段
    @Ignore
    @SerializedName("status")
    public StatusEntity status;
}
