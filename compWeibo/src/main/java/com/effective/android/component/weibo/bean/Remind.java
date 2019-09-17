package com.effective.android.component.weibo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 消息未读数
 Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class Remind {

    //新微博未读数
    @SerializedName("status")
    public int status;

    //新粉丝数
    @SerializedName("follower")
    public int follower;

    //新评论数
    @SerializedName("cmt")
    public int cmt;

    //新私信数
    @SerializedName("dm")
    public int dm;

    //新提及我的微博数
    @SerializedName("mention_status")
    public int mentionStatus;

    //新提及我的评论数
    @SerializedName("mention_cmt")
    public int mentionCmt;

    //微群消息未读数
    @SerializedName("group")
    public int group;

    //私有微群消息未读数
    @SerializedName("private_group")
    public int privateGroup;

    //新通知未读数
    @SerializedName("notice")
    public int notice;

    //新邀请未读数
    @SerializedName("invite")
    public int invite;

    //新勋章数
    @SerializedName("badge")
    public int badge;

    //相册消息未读数
    @SerializedName("photo")
    public int photo;

    //{{{3}}}
    @SerializedName("msgbox")
    public int msgbox;
}
