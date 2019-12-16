package com.effective.android.component.paccounts.view

import android.os.Bundle
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.util.DisplayUtils
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.paccounts.R
import com.effective.android.component.paccounts.Sdks
import com.effective.android.component.paccounts.vm.PaccountsViewModel
import com.effective.android.component.square.bean.Chapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.paccounts_fragment_laout.*

import com.effective.android.component.square.adapter.ChapterAdapter
import com.effective.android.component.square.bean.SelectableChapter
import com.effective.android.component.square.listener.OnEditListener


class PaccountsFragment : BaseVmFragment<PaccountsViewModel>() {

    private var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()
    var projects: List<Chapter>? = null
    val defaultSelectedSize = 5
    val defaultDisableDragIndex = 0
    private var chapterAdapter: ChapterAdapter? = null
    private var adapterView: View? = null

    override fun getViewModel(): Class<PaccountsViewModel> = PaccountsViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.paccounts_fragment_laout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageState.toLoading("正在加载")
        initView()
        fetchData()
    }

    private fun initView() {
        tabMore.setOnClickListener {
            adapterView?.visibility = View.VISIBLE
        }
    }

    private fun fetchData() {
        pageState.visibility = View.VISIBLE
        fetchProjectDisposable = viewModel.getPaccounts()
                .subscribe({
                    if (it.isSuccess) {
                        if (!it.data.isNullOrEmpty()) {
                            pageState.visibility = View.GONE
                            projects = it.data!!
                            initData(projects!!)
                        } else {
                            pageState.toEmpty("当前页面没有项目", "尝试刷新", Runnable {
                                pageState.toLoading("正在加载")
                                fetchData()
                            })
                        }
                    } else {
                        pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                            pageState.toLoading("正在加载")
                            fetchData()
                        })
                    }

                }, {
                    pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                        pageState.toLoading("正在加载")
                        fetchData()
                    })
                })
    }

    private fun initData(data: List<Chapter>) {
        val done: MutableList<SelectableChapter> = mutableListOf()
        val todo: MutableList<SelectableChapter> = mutableListOf()
        for ((startIndex, item) in data.withIndex()) {
            val warpChapter = SelectableChapter(item)
            warpChapter.draggable = startIndex != defaultDisableDragIndex
            warpChapter.selected = startIndex < defaultSelectedSize
            if (warpChapter.selected) {
                done.add(warpChapter)
            } else {
                todo.add(warpChapter)
            }
        }
        chapterAdapter = Sdks.blogSdk.getChapterAdapter(context!!)
        adapterView = chapterAdapter?.bindData(done, todo, object : OnEditListener {

            override fun onEdit() {
                //暂不需要处理
            }

            override fun onFinish(done: List<SelectableChapter>, todo: List<SelectableChapter>) {
                initPagerData(done)
            }
        })
        adapterView?.visibility = View.GONE
        adapterView?.setBackgroundColor(ResourceUtils.getColor(context!!,R.color.windowBackground))
        root.addView(adapterView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        initPagerData(done)
    }

    private fun initPagerData(data: List<SelectableChapter>) {
        //设置tab
        articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                var fragment: ArticleFragment? = articleFragments[position]
                if (fragment == null) {
                    fragment = ArticleFragment(data[position].data)
                    articleFragments[position] = fragment
                }
                return fragment
            }

            override fun getCount(): Int = data.size

            override fun getPageTitle(position: Int): CharSequence? = data[position].data.name
        }
        tabLayout.setupWithViewPager(articlePager)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchProjectDisposable != null && fetchProjectDisposable!!.isDisposed) {
            fetchProjectDisposable?.dispose()
        }
    }
}