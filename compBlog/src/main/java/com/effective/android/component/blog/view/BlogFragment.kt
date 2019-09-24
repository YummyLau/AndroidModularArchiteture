package com.effective.android.component.blog.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.R
import com.effective.android.component.blog.view.adapter.BlogArticleAdapter
import com.effective.android.component.blog.vm.BlogViewModel
import com.effective.android.service.kit.view.ArticleAdapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.blog_main_fragment_layout.*

class BlogFragment : BaseVmFragment<BlogViewModel>() {

    var pageNum: Int = 0
    var fetchDataDisposable: Disposable? = null
    lateinit var adapter: BlogArticleAdapter

    override fun getViewModel(): Class<BlogViewModel> = BlogViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.blog_main_fragment_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = BlogArticleAdapter()
        listContainer.setOnRefreshListener {
            fetchData(true)
        }
        listContainer.setOnLoadMoreListener {
            fetchData(false)
        }
        list.adapter = adapter
        fetchData(true)
    }

    private fun fetchData(boolean: Boolean) {
        pageNum = if (boolean) 0 else pageNum
        fetchDataDisposable = viewModel.getBlogList(pageNum)
                .subscribe({
                    if (it.isSuccess) {
                        if (pageNum == 0) {
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
                        listContainer.finishRefresh(2000, it.isSuccess, false)
                    } else {
                        listContainer.finishLoadMore(2000, it.isSuccess, false)
                    }
                }, {
                    if (boolean) {
                        listContainer.finishRefresh(2000, false, false)
                    } else {
                        listContainer.finishLoadMore(2000, false, false)
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