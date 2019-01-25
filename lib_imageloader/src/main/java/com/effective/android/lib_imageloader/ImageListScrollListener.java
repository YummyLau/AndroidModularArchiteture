package com.effective.android.lib_imageloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * 图片滚动时不加载
 * Created by yummyLau on 2018/5/28.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class ImageListScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = ImageListScrollListener.class.getSimpleName();

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Context context = recyclerView.getContext();
        switch (newState) {
            case SCROLL_STATE_IDLE: {
                resumeRequests(context);
                break;
            }
            case SCROLL_STATE_DRAGGING:
            case SCROLL_STATE_SETTLING: {
                pauseRequests(context);
                break;
            }
        }
    }

    private void resumeRequests(Context context) {
        try {
            Glide.with(context).resumeRequests();
            Log.d(TAG, "resumeRequests");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void pauseRequests(Context context) {
        try {
            Glide.with(context).pauseRequests();
            Log.d(TAG, "pauseRequests");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
