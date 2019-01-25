package com.effective.android.skin.bean;

/**
 * 皮肤项
 * Created by yummyLau on 2018/9/6.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class SkinItem {

    public String resourceName;
    public String alias;

    public SkinItem(String resourceName, String alias) {
        this.resourceName = resourceName;
        this.alias = alias;
    }
}
