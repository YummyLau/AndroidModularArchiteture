package com.effective.android.component.project.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.base.view.refreshlayout.SmartRefreshLayoutConfigure
import com.effective.android.component.square.adapter.ArticleAdapter
import com.effective.android.component.square.bean.Article
import com.effective.android.component.square.bean.Chapter
import com.effective.android.component.project.R
import com.effective.android.component.project.Sdks
import com.effective.android.component.project.vm.ArticleViewModel
import com.effective.android.service.skin.Skin
import com.effective.android.service.skin.SkinChangeListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.project_fragment_article_layout.*

class ArticleFragment(private val project: Chapter) : BaseVmFragment<ArticleViewModel>() {

    private var pageNum: Int = 1
    private var fetchDataDisposable: Disposable? = null
    private lateinit var adapter: ArticleAdapter<Article>
    private val skinChangeListener = object : SkinChangeListener {
        override fun onSkinChange(skin: Skin, success: Boolean) {
            if(success){
                SmartRefreshLayoutConfigure.refreshRefreshTheme(context!!, articleContainer)
            }
        }
    }

    override fun getViewModel(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_article_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Sdks.serviceSkin.addSkinChangeListener(skinChangeListener)
        adapter = Sdks.blogSdk.getArticleAdapter()
        articleList.adapter = adapter.getAdapter()
        articleList.addItemDecoration(adapter.getListItemDecoration(context!!,true))
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
        Sdks.serviceSkin.removeSkinChangeListener(skinChangeListener)
    }
}