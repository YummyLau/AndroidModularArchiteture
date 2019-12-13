package com.effective.android.component.blog
import android.content.Context
import androidx.fragment.app.Fragment
import com.effective.android.component.blog.adapter.ArticleAdapter
import com.effective.android.component.blog.bean.Article

interface ComponentBlogSdk {

    fun getMainFragment(): Fragment

    fun getMainName(): String

    fun <T>getArticleAdapter():ArticleAdapter<T>

    fun gotoDetailActivity(context: Context, article: Article)

}