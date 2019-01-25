package com.effective.android.base.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class BaseFragment : Fragment() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    var isFragmentVisible: Boolean = false
    var contentView: View? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFragmentVisible = isVisibleToUser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null) {
            contentView = inflater.inflate(getLayoutRes(), container, false)
        }
        return contentView
    }
}