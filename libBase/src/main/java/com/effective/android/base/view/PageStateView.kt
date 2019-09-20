package com.effective.android.base.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.effective.android.base.R
import kotlinx.android.synthetic.main.base_media_list_status_view.view.*

class PageStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.base_media_list_status_view, this, true)
        visibility = View.GONE
    }

    fun toLoading(statusTip: String) {
        status.visibility = if (TextUtils.isEmpty(statusTip)) View.GONE else View.VISIBLE
        status.text = statusTip
        loading.visibility = View.VISIBLE
        action.visibility = View.GONE
    }

    fun toEmpty(statusTip: String, actionTip: String, runnable: Runnable) {
        status.text = statusTip
        status.visibility = if (TextUtils.isEmpty(statusTip)) View.GONE else View.VISIBLE
        loading.visibility = View.GONE
        action.visibility = if (TextUtils.isEmpty(actionTip)) View.GONE else View.VISIBLE
        action.setOnClickListener {
            runnable.run()
        }
    }

    fun toAction(statusTip: String, actionTip: String, runnable: Runnable) {
        status.text = statusTip
        status.visibility = if (TextUtils.isEmpty(statusTip)) View.GONE else View.VISIBLE
        loading.visibility = View.GONE
        action.visibility = if (TextUtils.isEmpty(actionTip)) View.GONE else View.VISIBLE
        action.setOnClickListener {
            runnable.run()
        }
    }

    fun toFinish() {
        action.visibility = View.GONE
        loading.visibility = View.GONE
        status.visibility = View.GONE
    }
}
