package com.effective.android.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import skin.support.content.res.SkinCompatResources;
import skin.support.utils.SkinPreference;

/**
 * 换肤工具类
 * Created by yummyLau on 2018/9/6.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class SkinUtils {

    @ColorInt
    public static final int getColor(Context context, @ColorRes int color) {
        return SkinCompatResources.getColor(context, color);
    }

    public static final ColorStateList getColorStateList(Context context, @ColorRes int color) {
        return SkinCompatResources.getColorStateList(context, color);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawable) {
        return SkinCompatResources.getDrawableCompat(context, drawable);
    }

    public static String getCurrentSkinName() {
        return SkinPreference.getInstance().getSkinName();
    }
}
