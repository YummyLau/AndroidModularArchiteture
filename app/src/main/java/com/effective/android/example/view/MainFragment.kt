package com.effective.android.example.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.systemui.QMUIStatusBarHelper
import com.effective.android.example.R
import com.effective.android.example.Sdks
import com.effective.android.example.vm.HomeVm
import kotlinx.android.synthetic.main.app_fragment_home.*

class MainFragment : BaseVmFragment<HomeVm>() {

    private val fragmentList = mutableListOf<Fragment>()
    private val tabContainer = mutableListOf<View>()
    private val tabTip = mutableListOf<View>()
    private val tabIcon = mutableListOf<View>()

    override fun getViewModel(): Class<HomeVm> = HomeVm::class.java

    override fun getLayoutRes(): Int = R.layout.app_fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    private fun initData() {
        fragmentList.add(Sdks.componentTabHomeSdk.getMainFragment())
        fragmentList.add(Sdks.componentTabRecommendationSdk.getMainFragment())
        fragmentList.add(Sdks.componentMineSdk.getMainFragment())
        mainPager.adapter = object : FragmentPagerAdapter(childFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            /**
             * 当且仅当 findFragmentByTag 为 null 的时候才会调用，否则会使用系统缓存的fragments
             */
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int = fragmentList.size
        }
        mainPager.offscreenPageLimit = 3
    }

    private fun initView() {

        mainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectTab(position)
            }
        })
        tabContainer.add(homeTab)
        tabContainer.add(recommendTab)
        tabContainer.add(mineTab)
        tabTip.add(homeTabTip)
        tabTip.add(recommendTabTip)
        tabTip.add(mineTabTip)
        tabIcon.add(homeTabIcon)
        tabIcon.add(recommendTabIcon)
        tabIcon.add(mineTabIcon)
        selectTab(0)
        homeTab.setOnClickListener {
            mainPager.currentItem = 0
        }
        recommendTab.setOnClickListener {
            mainPager.currentItem = 1
        }
        mineTab.setOnClickListener {
            mainPager.currentItem = 2
        }
    }

    private fun selectTab(position: Int) {
        if (position <= 1) {
            QMUIStatusBarHelper.setStatusBarLightMode(activity)
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(activity)
        }
        for (index in tabContainer.indices) {
            val selected = index == position
            tabContainer[index].isSelected = selected
            tabTip[index].isSelected = selected
            tabIcon[index].isSelected = selected
        }
    }
}