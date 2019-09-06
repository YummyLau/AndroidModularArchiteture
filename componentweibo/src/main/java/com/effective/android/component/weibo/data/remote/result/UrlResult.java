package com.effective.android.component.weibo.data.remote.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.effective.android.component.weibo.bean.UrlShort;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/17.
 */

public class UrlResult extends ApiResult{

    @SerializedName("urls")
    public List<UrlShort> urls;
}
