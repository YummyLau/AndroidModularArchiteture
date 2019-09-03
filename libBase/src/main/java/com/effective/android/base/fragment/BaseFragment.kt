package com.effective.android.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class BaseFragment : VisibleFragment() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null) {
            contentView = inflater.inflate(getLayoutRes(), container, false)
        }
        return contentView
    }
}