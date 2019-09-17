package com.effective.android.component.blog

import androidx.fragment.app.Fragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentBlogSdk::class])
class ComponentBlogImpl :ComponentBlogSdk{

    override fun getMainFragment(): Fragment {
        return Fragment()
    }
}