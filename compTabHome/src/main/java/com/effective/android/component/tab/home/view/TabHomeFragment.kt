package com.effective.android.component.tab.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.systemui.QMUIStatusBarHelper
import com.effective.android.component.tab.home.R
import com.effective.android.component.tab.home.Sdks
import com.effective.android.component.tab.home.vm.TabHomeVm
import kotlinx.android.synthetic.main.tabh_fragment_home_layout.*

class TabHomeFragment : BaseVmFragment<TabHomeVm>() {

    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()

    override fun getViewModel(): Class<TabHomeVm> = TabHomeVm::class.java

    override fun getLayoutRes(): Int  = R.layout.tabh_fragment_home_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initView()
    }

    private fun initData(){
        fragmentList.add(Sdks.blogSdk.getMainFragment())
        titleList.add(Sdks.blogSdk.getMainName())

        fragmentList.add(Sdks.paccountsSdk.getMainFragment())
        titleList.add(Sdks.paccountsSdk.getMainName())

        fragmentList.add(Sdks.systemSdk.getMainFragment())
        titleList.add(Sdks.systemSdk.getMainName())

        fragmentList.add(Sdks.projectSdk.getMainFragment())
        titleList.add(Sdks.projectSdk.getMainName())
    }

    private fun initView(){
        icSearch.setOnClickListener {

        }
        pager.offscreenPageLimit = 4

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })

        val pagerAdapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment = fragmentList[position]

            override fun getCount(): Int  = fragmentList.size

            override fun getPageTitle(position: Int): CharSequence?  = titleList[position]
        }
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }
}