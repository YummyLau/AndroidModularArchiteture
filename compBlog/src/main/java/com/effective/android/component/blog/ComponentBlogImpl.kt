package com.effective.android.component.blog

import androidx.fragment.app.Fragment
import com.effective.android.component.blog.adapter.ArticleAdapter
import com.effective.android.component.blog.view.BlogFragment
import com.effective.android.component.blog.view.adapter.BlogArticleAdapter
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentBlogSdk::class])
class ComponentBlogImpl : ComponentBlogSdk {

    private var blogFragment: Fragment? = null

    override fun getMainFragment(): Fragment {
        if (blogFragment == null) {
            blogFragment = BlogFragment()
        }
        return blogFragment!!
    }

    override fun <T> getArticleAdapter(): ArticleAdapter<T> {
        return BlogArticleAdapter() as ArticleAdapter<T>
    }
}