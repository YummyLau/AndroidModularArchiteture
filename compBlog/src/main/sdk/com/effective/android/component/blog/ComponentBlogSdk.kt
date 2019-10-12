package com.effective.android.component.blog

import android.content.Context
import androidx.fragment.app.Fragment

interface ComponentBlogSdk {

    fun getMainFragment(): Fragment

    fun toBlogDetailPager(context: Context, string: String)
}