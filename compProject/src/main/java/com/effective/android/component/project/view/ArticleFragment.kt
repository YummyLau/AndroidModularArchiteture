package com.effective.android.component.project.view

import android.os.Bundle
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.toast.ToastUtils
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.project.R
import com.effective.android.component.project.bean.Project
import com.effective.android.component.project.view.adapter.ArticleAdapter
import com.effective.android.component.project.vm.ArticleViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.project_fragment_article_layout.*

class ArticleFragment(private val project: Project) : BaseVmFragment<ArticleViewModel>() {

    var pageNum: Int = 1
    var fetchDataDisposable: Disposable? = null
    var subFragmentListStatus: ProjectFragment.SubFragmentListStatus? = null
    lateinit var adapter: ArticleAdapter

    override fun getViewModel(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_article_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ArticleAdapter()
        articleList.adapter = adapter
        articleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                subFragmentListStatus?.onListStatus(articleList.isOnTop())
            }
        })

        fetchData()
    }

    fun attachStatus(subFragmentListStatus: ProjectFragment.SubFragmentListStatus) {
        this.subFragmentListStatus = subFragmentListStatus
        if(articleList != null){
            subFragmentListStatus.onListStatus(articleList.isOnTop())
        }
    }

    fun refreshData(subFragmentFresh: ProjectFragment.SubFragmentFresh) {
        pageNum = 1
        fetchData(subFragmentFresh)
    }


    private fun fetchData(subFragmentFresh: ProjectFragment.SubFragmentFresh? = null) {
        fetchDataDisposable = viewModel.getArticles(pageNum, project.id)
                .subscribe({
                    subFragmentFresh?.onFinish()
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
                    ToastUtils.show(context!!, if (it.isSuccess) "加载成功" else "加载失败")
                }, {
                    subFragmentFresh?.onFinish()
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