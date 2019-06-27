package example.basiclib.widget.imageviewer;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import example.basiclib.R;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class ImageViewerAdapter extends PagerAdapter {

    public int mStartPosition;
    public List<String> mList;
    private View mCurrentView;
    private ImageViewerActivity mViewerActivity;

    public ImageViewerAdapter(ImageViewerActivity activity, List<String> list, int position) {
        mList = list;
        mViewerActivity = activity;
        mStartPosition = position;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
    }

    public View getCurrentView() {
        return mCurrentView;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView =
                PhotoViewCreator.createPhotoView(container.getContext(), mList.get(position));
        if (ImageViewer.supportTransition()) {
            photoView.setTransitionName(mViewerActivity.getString(R.string.transition_name, position));
            if (position == mStartPosition && ImageViewer.supportTransition()) {
                setStartPostTransition(photoView);
            }
        }
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStartPostTransition(final View sharedView) {
        sharedView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    // @Override
                    public boolean onPreDraw() {
                        sharedView.getViewTreeObserver().removeOnPreDrawListener(this);
                        mViewerActivity.supportStartPostponedEnterTransition();
                        return false;
                    }
                });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
