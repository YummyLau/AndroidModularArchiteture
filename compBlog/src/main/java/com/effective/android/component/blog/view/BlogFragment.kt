package com.effective.android.component.blog.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.toast.ToastUtils
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.R
import com.effective.android.component.blog.view.adapter.ArticleAdapter
import com.effective.android.component.blog.vm.BlogViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.blog_main_fragment_layout.*

class BlogFragment : BaseVmFragment<BlogViewModel>() {

    var pageNum: Int = 0
    var fetchDataDisposable: Disposable? = null
    lateinit var adapter: ArticleAdapter

    override fun getViewModel(): Class<BlogViewModel> = BlogViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.blog_main_fragment_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ArticleAdapter()
        listContainer.setOnRefreshListener {
            pageNum = 0
            fetchData()
        }
        list.adapter = adapter
        fetchData()
    }

    private fun fetchData() {
        fetchDataDisposable = viewModel.getBlogList(pageNum)
                .map {
                    listContainer.isRefreshing = false
                    it
                }.subscribe({
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
                    ToastUtils.show(context!!, if (it.isSuccess) "加载成功" else "加载失败")
                }, {
                    ToastUtils.show(context!!, "加载失败")
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchDataDisposable != null && fetchDataDisposable!!.isDisposed) {
            fetchDataDisposable?.dispose()
        }
    }
}