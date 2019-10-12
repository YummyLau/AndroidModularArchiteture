package com.effective.android.component.blog
import androidx.fragment.app.Fragment
import com.effective.android.component.blog.adapter.ArticleAdapter

interface ComponentBlogSdk {

    fun getMainFragment(): Fragment

    fun <T>getArticleAdapter():ArticleAdapter<T>
}