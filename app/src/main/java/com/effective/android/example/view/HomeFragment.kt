package com.effective.android.example.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.example.R
import com.effective.android.example.Sdks
import com.effective.android.example.vm.HomeVm
import kotlinx.android.synthetic.main.app_fragment_home.*

class HomeFragment : BaseVmFragment<HomeVm>() {

    private val fragmentList = mutableListOf<Fragment>()

    override fun getViewModel(): Class<HomeVm> = HomeVm::class.java

    override fun getLayoutRes(): Int = R.layout.app_fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        homePager.adapter = object : FragmentPagerAdapter(childFragmentManager!!) {

            /**
             * 当且仅当 findFragmentByTag 为 null 的时候才会调用，否则会使用系统缓存的fragments
             */
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int = fragmentList.size
        }
        homePager.offscreenPageLimit = 4

    }
}