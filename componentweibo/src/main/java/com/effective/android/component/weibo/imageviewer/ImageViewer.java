package com.effective.android.component.weibo.imageviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 外部调用入口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ImageViewer {

    public static final String TAG = ImageViewer.class.getSimpleName();
    public static final String INTENT_EXIT_POSITION = "intent_exit_position";

    public static void start(Activity activity, List<String> urls, int startIndex) {
        start(activity, urls, startIndex, null);
    }

    public static void start(Activity activity, List<String> urls, int startIndex, @Nullable View shareView) {
        if (activity == null || urls == null || urls.isEmpty() || urls.size() <= startIndex) {
            return;
        }
        Intent intent = new Intent(activity, ImageViewerActivity.class);
        intent.putExtra(ImageViewerActivity.INTENT_START, startIndex);
        intent.putStringArrayListExtra(ImageViewerActivity.INTENT_URLS, (ArrayList<String>) urls);
        if (supportTransition()) {
            if (shareView == null) {
                activity.startActivity(intent);
            } else {
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, shareView, shareView.getTransitionName());
                activity.startActivityForResult(intent, 0, options.toBundle());
            }
        } else {
            activity.startActivity(intent);
        }
    }

    public static int getExitPostion(int resultCode, Intent data, int defaultPosition) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            return data.getIntExtra(INTENT_EXIT_POSITION, defaultPosition);
        } else {
            return defaultPosition;
        }
    }

    public static void sendExitResult(ImageViewerActivity activity, int exitPosition) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXIT_POSITION, exitPosition);
        activity.setResult(activity.RESULT_OK, intent);
    }


    public static boolean supportTransition() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
