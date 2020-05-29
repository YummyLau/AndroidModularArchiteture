package com.effective.android.component.system.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.view.refreshlayout.SmartRefreshLayoutConfigure
import com.effective.android.component.square.adapter.ChapterAdapter
import com.effective.android.component.system.vm.SystemViewModel
import com.effective.android.component.system.R
import com.effective.android.component.square.bean.Chapter
import com.effective.android.component.square.bean.SelectableChapter
import com.effective.android.component.square.listener.OnEditListener
import com.effective.android.component.system.Sdks
import com.effective.android.service.skin.Skin
import com.effective.android.service.skin.SkinChangeListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.system_fragment_laout.*
import kotlinx.android.synthetic.main.system_holder_article_layout.*
import skin.support.widget.SkinCompatSupportable

class SystemFragment: BaseVmFragment<SystemViewModel>() {

    private var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleParentFragment> = HashMap()
    var chapters: List<Chapter>? = null
    val defaultSelectedSize = 5
    val defaultDisableDragIndex = 0
    private var chapterAdapter: ChapterAdapter? = null
    private var adapterView: View? = null
    private val skinChangeListener = object : SkinChangeListener {

        override fun onSkinChange(skin: Skin, success: Boolean) {
            if (success && adapterView is SkinCompatSupportable) {
                (adapterView as SkinCompatSupportable).applySkin()
            }
        }
    }

    override fun getViewModel(): Class<SystemViewModel> = SystemViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.system_fragment_laout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageState.toLoading("正在加载")
        initView()
        initData()
    }

    private fun initView() {
        tabMore.setOnClickListener {
            adapterView?.visibility = View.VISIBLE
        }
    }

    private fun initData() {
        pageState.visibility = View.VISIBLE
        fetchProjectDisposable = viewModel.getTreeChapters()
                .subscribe({
                    if (it.isSuccess) {
                        if (!it.data.isNullOrEmpty()) {
                            pageState.visibility = View.GONE
                            chapters = it.data!!
                            initData(chapters!!)
                        } else {
                            pageState.toEmpty("当前页面没有项目", "尝试刷新", Runnable {
                                pageState.toLoading("正在加载")
                                initData()
                            })
                        }
                    } else {
                        pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                            pageState.toLoading("正在加载")
                            initData()
                        })
                    }

                }, {
                    pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                        pageState.toLoading("正在加载")
                        initData()
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

            override fun onCancel() {
                //暂不需要处理
            }

            override fun onEdit() {
                //暂不需要处理
            }

            override fun onFinish(done: List<SelectableChapter>, todo: List<SelectableChapter>) {
                initPagerData(done)
            }
        })
        adapterView?.visibility = View.GONE
        systemRoot.addView(adapterView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        Sdks.serviceSkin.addSkinChangeListener(skinChangeListener)
        initPagerData(done)
    }

    private fun initPagerData(data: List<SelectableChapter>) {
        articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                var fragment: ArticleParentFragment? = articleFragments[position]
                if (fragment == null) {
                    fragment = ArticleParentFragment(data[position].data.children)
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
        Sdks.serviceSkin.removeSkinChangeListener(skinChangeListener)
    }
}