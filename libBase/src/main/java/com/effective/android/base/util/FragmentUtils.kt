package com.effective.android.base.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentUtils {

    @JvmStatic
    fun replace(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, tag: String? = null) {
        fragmentManager.beginTransaction().replace(containerId, fragment, tag).commit()
    }

    @JvmStatic
    fun add(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, tag: String? = null) {
        fragmentManager.beginTransaction().add(containerId, fragment, tag).commit()
    }

}