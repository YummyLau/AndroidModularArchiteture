package com.effective.android.example

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.activity.BaseActivity
import com.effective.android.base.systemui.StatusbarHelper
import kotlinx.android.synthetic.main.app_activity_main_layout.*


class MainActivity : BaseActivity(){

    private val fragmentList = mutableListOf<Fragment>()

    override fun getLayoutRes(): Int {
        return R.layout.app_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.translucentStatusBar(this)
        initData()
        initView()
    }

    private fun initData() {
        fragmentList.add(Sdks.componentBlogSdk.getMainFragment())
        fragmentList.add(Sdks.componentProjectSdk.getMainFragment())
        fragmentList.add(Sdks.componentPaccountsSdk.getMainFragment())
        fragmentList.add(Sdks.componentSystemSdk.getMainFragment())
        fragmentList.add(Sdks.componentMineSdk.getMainFragment())
    }

    private fun initView() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            /**
             * 当且仅当 findFragmentByTag 为 null 的时候才会调用，否则会使用系统缓存的fragments
             */
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int = fragmentList.size
        }
        viewPager.offscreenPageLimit = 4
        bottomTab.setupWithViewPager(viewPager)
    }
}

