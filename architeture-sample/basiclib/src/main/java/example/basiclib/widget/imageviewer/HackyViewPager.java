package example.basiclib.widget.imageviewer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决项目框架bug
 * https://github.com/chrisbanes/PhotoView/blob/master/sample/src/main/java/com/github/chrisbanes/photoview/sample/HackyViewPager.java
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class HackyViewPager extends ViewPager {

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
