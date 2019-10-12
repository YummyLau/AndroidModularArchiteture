package com.effective.android.component.paccounts.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.adapter.ArticleAdapter
import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.bean.Chapter
import com.effective.android.component.paccounts.R
import com.effective.android.component.paccounts.Sdks
import com.effective.android.component.paccounts.vm.ArticleViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.paccounts_fragment_article_layout.*

class ArticleFragment(private val chapter: Chapter) : BaseVmFragment<ArticleViewModel>() {

    var pageNum: Int = 1
    var fetchDataDisposable: Disposable? = null
    lateinit var adapter: ArticleAdapter<Article>

    override fun getViewModel(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.paccounts_fragment_article_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = Sdks.blogSdk.getArticleAdapter()
        articleList.adapter = adapter.getAdapter()
        articleContainer.setOnRefreshListener {
            fetchData(true)
        }
        articleContainer.setOnLoadMoreListener {
            fetchData(false)
        }
        fetchData(true)
    }

    private fun fetchData(boolean: Boolean) {
        pageNum = if (boolean) 1 else pageNum
        fetchDataDisposable = viewModel.getArticles(chapter.id, pageNum)
                .subscribe({
                    if (it.isSuccess) {
                        if (pageNum == 1) {
                            adapter.replaceData(it.data!!.data)
                        } else {
                            var list = mutableListOf<Article>()
                            list.addAll(adapter.getData())
                            list.addAll(it.data!!.data)
                            adapter.updateData(list)
                        }
                        pageNum++
                    }
                    if (boolean) {
                        articleContainer.finishRefresh(2000, it.isSuccess, false)
                    } else {
                        articleContainer.finishLoadMore(2000, it.isSuccess, false)
                    }
                }, {
                    if (boolean) {
                        articleContainer.finishRefresh(2000, false, false)
                    } else {
                        articleContainer.finishLoadMore(2000, false, false)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchDataDisposable != null && fetchDataDisposable!!.isDisposed) {
            fetchDataDisposable?.dispose()
        }
    }
}