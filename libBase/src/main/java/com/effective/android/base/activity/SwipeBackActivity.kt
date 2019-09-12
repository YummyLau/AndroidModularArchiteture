package com.effective.android.base.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.effective.android.base.activity.swipeback.SwipeBackActivityBase
import com.effective.android.base.activity.swipeback.SwipeBackActivityHelper
import com.effective.android.base.activity.swipeback.SwipeBackLayout
import com.effective.android.base.activity.swipeback.SwipeBackUtils

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class SwipeBackActivity : BaseActivity(), SwipeBackActivityBase {

    private var mHelper: SwipeBackActivityHelper? = null
    private var isResume: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportSwipeBack()) {
            mHelper = SwipeBackActivityHelper(this)
            mHelper?.onActivityCreate()
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        if (supportSwipeBack()) {
            mHelper?.onPostCreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (supportSwipeBack()) {
            mHelper?.onDestroy()
        }
    }

    fun supportSwipeBack(): Boolean {
        return true
    }

    fun isResume(): Boolean {
        return isResume
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return if (supportSwipeBack()) {
            this.mHelper?.swipeBackLayout!!
        } else {
            throw IllegalStateException("supportSwipeBack must return true")
        }
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        if (supportSwipeBack()) {
            this.getSwipeBackLayout().setEnableGesture(enable)
        }
    }

    override fun scrollToFinishActivity() {
        if (supportSwipeBack()) {
            SwipeBackUtils.convertActivityToTranslucent(this)
            this.getSwipeBackLayout().scrollToFinishActivity()
        }
    }
}
