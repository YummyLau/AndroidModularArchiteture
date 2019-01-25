package com.effective.android.lib_imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.effective.android.base.util.system.ActivityUtils;
import com.effective.android.imageloader.interfaces.IImageLoader;

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class ImageLoader implements IImageLoader {

    public Context isValidContextForGlide(@NonNull ImageView imageView) {
        if (imageView == null || imageView.getContext() == null) {
            return null;
        }
        Context context = imageView.getContext();
        Activity activity = ActivityUtils.scanForActivity(context);
        if (activity.isDestroyed() || activity.isFinishing()) {
            return null;
        }
        return context;
    }

    @Override
    public Bitmap loadBitmap(@NonNull Context context, String url, int width, int height) {
        try {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .fitCenter();

            return Glide.with(context)
                    .asBitmap()
                    .apply(options)
                    .load(url).submit(width, height).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clearDisplayRequest(ImageView imageView) {
        Context context = isValidContextForGlide(imageView);
        if (context == null)
            return;
        Glide.with(imageView).clear(imageView);
    }

    @Override
    public void displayGradientImage(final ImageView imageView, String url) {
        Context context = isValidContextForGlide(imageView);
        if (context == null)
            return;

        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap bgBitmap = resource.copy(Bitmap.Config.ARGB_8888, true);
                Palette.from(bgBitmap).maximumColorCount(24).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int vibrant = palette.getLightVibrantColor(Color.BLACK);
                        int vibrantLight = palette.getVibrantColor(Color.BLACK);
                        int vibrantDark = palette.getDarkVibrantColor(Color.BLACK);
                        int alphaVibrant = Color.argb(175, Color.red(vibrant), Color.green(vibrant), Color.blue(vibrant));
                        int alphaVibrantLight = Color.argb(175, Color.red(vibrantLight), Color.green(vibrantLight), Color.blue(vibrantLight));
                        int alphaVibrantDark = Color.argb(175, Color.red(vibrantDark), Color.green(vibrantDark), Color.blue(vibrantDark));

                        int colors[] = {alphaVibrant, alphaVibrantLight, alphaVibrantDark};
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
                        imageView.setImageDrawable(gradientDrawable);
                    }
                });
            }
        });
    }

    @Override
    public void displayRoundImage(ImageView imageView, String url, int cornerPx, @DrawableRes int placeholder, @DrawableRes int error) {
        Context context = isValidContextForGlide(imageView);
        if (context == null)
            return;

        RequestOptions options = new RequestOptions()
                .error(placeholder)
                .placeholder(error)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new RoundedCorners(cornerPx));

        Glide.with(context).asBitmap().apply(options).load(url).into(imageView);
    }

    @Override
    public void displayGifImage(ImageView imageView, String url, @DrawableRes int placeholder, @DrawableRes int error) {
        Context context = isValidContextForGlide(imageView);
        if (context == null)
            return;
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(error)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .set(GifOptions.DISABLE_ANIMATION, false);
        Glide.with(context).asGif().apply(options).load(url).into(imageView);
    }
}
