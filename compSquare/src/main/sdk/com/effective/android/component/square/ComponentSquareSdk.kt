package com.effective.android.component.square
import android.content.Context
import androidx.fragment.app.Fragment
import com.effective.android.component.square.adapter.ArticleAdapter
import com.effective.android.component.square.bean.Article

interface ComponentSquareSdk {

    fun getMainFragment(): Fragment

    fun getMainName(): String

    fun <T>getArticleAdapter(): ArticleAdapter<T>

    fun gotoDetailActivity(context: Context, article: Article)

}