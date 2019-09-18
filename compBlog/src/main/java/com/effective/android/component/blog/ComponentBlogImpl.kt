package com.effective.android.component.blog

import androidx.fragment.app.Fragment
import com.effective.android.component.blog.view.BlogFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentBlogSdk::class])
class ComponentBlogImpl : ComponentBlogSdk {

    var blogFragment: Fragment? = null

    override fun getMainFragment(): Fragment {
        if (blogFragment == null) {
            blogFragment = BlogFragment()
        }
        return blogFragment!!
    }
}