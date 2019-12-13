package com.effective.android.example.view

import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.example.R
import com.effective.android.example.vm.SplashVm
import kotlinx.android.synthetic.main.app_fragment_splash.*

/**
 * 闪屏模块
 * created by yummylau on 2019/12/13
 */
class SplashFragment : BaseVmFragment<SplashVm>() {

    override fun getViewModel(): Class<SplashVm> = SplashVm::class.java

    override fun getLayoutRes(): Int = R.layout.app_fragment_splash

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadAd()
    }

    private fun loadAd(){
        splashSkip.setOnClickListener {
            (activity as MainActivity).toHome()
        }
//        splashImage.setImageDrawable(R.drawable.app_ic_blog)
    }
}