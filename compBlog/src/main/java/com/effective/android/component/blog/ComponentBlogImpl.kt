package com.effective.android.component.blog

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.adapter.ArticleAdapter
import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.bean.BlogArticle
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
        return ArticleAdapterImpl()
    }
}

class ArticleAdapterImpl<T> : BlogArticleAdapter(), ArticleAdapter<T> {

    override fun updateData(mutableList: MutableList<T>) {
        val toList = mutableListOf<IMediaItem>()
        for (t in mutableList) {
            if (t is Article) {
                toList.add(BlogArticle(t))
            }
        }
        super.update(toList)
    }

    override fun replaceData(mutableList: MutableList<T>) {
        val toList = mutableListOf<IMediaItem>()
        for (t in mutableList) {
            if (t is Article) {
                toList.add(BlogArticle(t))
            }
        }
        super.replace(toList)
    }

    override fun getData(): MutableList<T> {
        val toList = mutableListOf<T>()
        for (t in dataList) {
            toList.add(t as T)
        }
        return toList
    }

    override fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return this as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}