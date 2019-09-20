package com.effective.android.component.project.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.project.R
import com.effective.android.component.project.bean.Project
import com.effective.android.component.project.vm.ProjectViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.project_fragment_laout.*

class ProjectFragment : BaseVmFragment<ProjectViewModel>() {

    var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()
    var projects: List<Project>? = null
    private val subFragmentFresh = object : SubFragmentFresh {
        override fun onFinish() {
            articleContainer.isRefreshing = false
        }
    }

    private val subFragmentListStatus = object : SubFragmentListStatus {
        override fun onListStatus(onTop: Boolean) {
            articleContainer.isEnabled = onTop
        }
    }

    override fun getViewModel(): Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_laout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleContainer.setOnRefreshListener {
            if (projects == null) {
                articleContainer.isRefreshing = false
                return@setOnRefreshListener
            }

            if (articleFragments[articlePager.currentItem] == null) {
                articleContainer.isRefreshing = false
                return@setOnRefreshListener
            }
            articleFragments[articlePager.currentItem]!!.refreshData(subFragmentFresh)
        }
        initData()
    }

    private fun initData() {
        fetchProjectDisposable = viewModel.getProjects()
                .subscribe({
                    if (it.isSuccess) {
                        if (!it.data.isNullOrEmpty()) {
                            projects = it.data!!
                            articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

                                override fun getItem(position: Int): Fragment {
                                    var fragment: ArticleFragment? = articleFragments[position]
                                    if (fragment == null) {
                                        fragment = ArticleFragment(projects!![position])
                                        articleFragments[position] = fragment
                                    }
                                    fragment.attachStatus(subFragmentListStatus)
                                    return fragment
                                }

                                override fun getCount(): Int = projects!!.size

                                override fun getPageTitle(position: Int): CharSequence? = projects!![position].name
                            }
                            tabLayout.setViewPager(articlePager)
                        } else {
                            //todo 页面为空场景
                        }
                    } else {
                        //todo 页面失败场景
                    }

                }, {
                    //todo 页面失败场景
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchProjectDisposable != null && fetchProjectDisposable!!.isDisposed) {
            fetchProjectDisposable?.dispose()
        }
    }

    interface SubFragmentFresh {
        fun onFinish()
    }

    interface SubFragmentListStatus {
        fun onListStatus(onTop: Boolean)
    }
}