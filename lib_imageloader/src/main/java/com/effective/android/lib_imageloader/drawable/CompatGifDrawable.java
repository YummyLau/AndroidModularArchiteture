package com.effective.android.lib_imageloader.drawable;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.effective.android.base.util.ReflectionUtils;

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class CompatGifDrawable extends GifDrawable {

    private GifAnimationListener mGifAnimationListener;

    public CompatGifDrawable(Context context, GifDecoder gifDecoder, Transformation<Bitmap> frameTransformation, int targetFrameWidth, int targetFrameHeight, Bitmap firstFrame) {
        super(context, gifDecoder, frameTransformation, targetFrameWidth, targetFrameHeight, firstFrame);
    }

    public void setGifAnimationListener(GifAnimationListener gifAnimationListener) {
        mGifAnimationListener = gifAnimationListener;
    }

    @Override
    public void onFrameReady() {
        super.onFrameReady();
        Object parentLoopCount = ReflectionUtils.getFieldValue(this, "loopCount");
        Object parentMaxLoopCount = ReflectionUtils.getFieldValue(this, "maxLoopCount");

        if (parentLoopCount != null && parentLoopCount instanceof Integer
                && parentMaxLoopCount != null && parentMaxLoopCount instanceof Integer) {
            if (((Integer) parentMaxLoopCount) != LOOP_FOREVER && ((Integer) parentLoopCount) >= ((Integer) parentMaxLoopCount)) {
                if (mGifAnimationListener != null) {
                    mGifAnimationListener.onComplete();
                }
            }
        }
    }

    interface GifAnimationListener {
        void onComplete();
    }
}
