package com.effective.android.component.system.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseFragment
import com.effective.android.component.blog.Chapter
import com.effective.android.component.system.R
import kotlinx.android.synthetic.main.system_fragment_laout.*

class ArticleParentFragment(private val chapters: MutableList<Chapter>) : BaseFragment() {

    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()

    override fun getLayoutRes(): Int = R.layout.system_fragment_article_parent_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                var fragment: ArticleFragment? = articleFragments[position]
                if (fragment == null) {
                    fragment = ArticleFragment(chapters[position])
                    articleFragments[position] = fragment
                }
                return fragment
            }

            override fun getCount(): Int = chapters.size

            override fun getPageTitle(position: Int): CharSequence? = chapters[position].name
        }
        tabLayout.setViewPager(articlePager)
    }
}