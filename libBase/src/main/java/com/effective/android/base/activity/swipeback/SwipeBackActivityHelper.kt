package com.effective.android.base.activity.swipeback

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.effective.android.base.R


class SwipeBackActivityHelper(private var mActivity: Activity?) {

    var swipeBackLayout: SwipeBackLayout? = null
        private set

    fun onActivityCreate() {
        mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mActivity?.window?.decorView?.background = null
        swipeBackLayout = LayoutInflater.from(mActivity).inflate(
                R.layout.widget_swipeback_layout, null) as SwipeBackLayout
    }

    fun onPostCreate() {
        if (mActivity != null) {
            swipeBackLayout?.attachToActivity(mActivity!!)
        }
    }

    fun findViewById(id: Int): View? {
        return if (swipeBackLayout != null) {
            swipeBackLayout!!.findViewById(id)
        } else null
    }

    fun onDestroy() {
        mActivity = null
        swipeBackLayout!!.dettachToActivity()
        swipeBackLayout = null
    }
}
