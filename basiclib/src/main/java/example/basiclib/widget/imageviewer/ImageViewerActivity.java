package example.basiclib.widget.imageviewer;


import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import example.basiclib.R;
import example.basiclib.activity.BaseBindingActivity;
import example.basiclib.databinding.ActivityImageviewerLayoutBinding;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ImageViewerActivity extends BaseBindingActivity<ActivityImageviewerLayoutBinding> {

    public static final String INTENT_URLS = "intentUrls";
    public static final String INTENT_START = "intentStart";

    private List<String> mUrls;
    private int startIndex;
    private Intent mIntent;
    private ImageViewerAdapter mPagerAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_imageviewer_layout;
    }

    @Override
    public int getStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();

        mIntent = getIntent();
        mUrls = mIntent.getStringArrayListExtra(INTENT_URLS);
        startIndex = mIntent.getIntExtra(INTENT_START, 0);
        mPagerAdapter = new ImageViewerAdapter(this, mUrls, startIndex);
        dataBinding.viewpager.setAdapter(mPagerAdapter);
        dataBinding.viewpager.setCurrentItem(startIndex);
        dataBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dataBinding.pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void finishAfterTransition() {
        int lastPosition = dataBinding.viewpager.getCurrentItem();
        ImageViewer.sendExitResult(this, lastPosition);
        if (startIndex != lastPosition) {
            //如果当前已经移动过view项
            dataBinding.viewpager.getCurrentItem();
            setSharedElementCallback(mPagerAdapter.getCurrentView());
        }
        super.finishAfterTransition();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setSharedElementCallback(final View view) {
        setEnterSharedElementCallback(new SharedElementCallback() {
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();
                names.add(view.getTransitionName());
                sharedElements.put(view.getTransitionName(), view);
            }
        });
    }
}
