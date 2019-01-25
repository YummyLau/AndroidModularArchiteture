package com.effective.android.lib_imageloader.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 * todo 缓存规则，gif圆角等难点
 */
public interface IImageLoader {

    void clearDisplayRequest(ImageView imageView);

    Bitmap loadBitmap(Context context, String url, int width, int height);

    void displayGradientImage(ImageView imageView, String url);

    void displayRoundImage(ImageView imageView, String url, int cornerPx, @DrawableRes int placeholder, @DrawableRes int error);

    void displayGifImage(ImageView imageView, String url, @DrawableRes int placeholder, @DrawableRes int error);
}
