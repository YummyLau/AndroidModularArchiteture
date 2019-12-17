package com.effective.android.example.view

import android.os.Bundle
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

    override fun getViewModel(): Class<HomeVm> = HomeVm::class.java

    override fun getLayoutRes(): Int = R.layout.app_fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        fragmentList.add(Sdks.componentTabHomeSdk.getMainFragment())
        fragmentList.add(Sdks.componentTabRecommendationSdk.getMainFragment())
        fragmentList.add(Sdks.componentMineSdk.getMainFragment())
    }

    private fun initView() {
        mainPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            /**
             * 当且仅当 findFragmentByTag 为 null 的时候才会调用，否则会使用系统缓存的fragments
             */
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int = fragmentList.size
        }
        mainPager.offscreenPageLimit = 4
        mainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position <= 1){
                    QMUIStatusBarHelper.setStatusBarLightMode(activity)
                }else{
                    QMUIStatusBarHelper.setStatusBarDarkMode(activity)
                }
            }
        })
        QMUIStatusBarHelper.setStatusBarLightMode(activity)
        homeTab.setOnClickListener {
            mainPager.currentItem = 0
        }
        homeTab1.setOnClickListener {
            mainPager.currentItem = 1
        }
        homeTab2.setOnClickListener {
            mainPager.currentItem = 2
        }
    }
}