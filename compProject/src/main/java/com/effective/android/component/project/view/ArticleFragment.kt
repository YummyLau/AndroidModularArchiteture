package com.effective.android.component.project.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.project.R
import com.effective.android.component.blog.Chapter
import com.effective.android.component.project.view.adapter.ArticleAdapter
import com.effective.android.component.project.vm.ArticleViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.project_fragment_article_layout.*

class ArticleFragment(private val project: com.effective.android.component.blog.Chapter) : BaseVmFragment<ArticleViewModel>() {

    var pageNum: Int = 1
    var fetchDataDisposable: Disposable? = null
    lateinit var adapter: ArticleAdapter

    override fun getViewModel(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_article_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ArticleAdapter()
        articleList.adapter = adapter
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
        fetchDataDisposable = viewModel.getArticles(pageNum, project.id)
                .subscribe({
                    if (it.isSuccess) {
                        if (pageNum == 1) {
                            adapter.replace(it.data!!.data)
                        } else {
                            var list = mutableListOf<IMediaItem>()
                            list.addAll(adapter.dataList)
                            list.addAll(it.data!!.data)
                            adapter.update(list)
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