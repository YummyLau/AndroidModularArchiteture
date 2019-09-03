package com.effective.android.base.activity.swipeback

import android.app.Activity

import java.lang.ref.WeakReference


/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class SwipeBackListenerActivityAdapter(activity: Activity) : SwipeBackLayout.SwipeListenerEx {
    private val mActivity: WeakReference<Activity>

    init {
        mActivity = WeakReference(activity)
    }

    override fun onScrollStateChange(state: Int, scrollPercent: Float) {

    }

    override fun onEdgeTouch(edgeFlag: Int) {
        val activity = mActivity.get()
        if (null != activity) {
            SwipeBackUtils.convertActivityToTranslucent(activity)
        }
    }

    override fun onScrollOverThreshold() {

    }

    override fun onContentViewSwipedBack() {
        val activity = mActivity.get()
        if (null != activity && !activity.isFinishing) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }
}
