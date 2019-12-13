package com.effective.android.example.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.activity.BaseActivity
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.base.util.FragmentUtils
import com.effective.android.example.R
import com.effective.android.example.Sdks
import kotlinx.android.synthetic.main.app_activity_main_layout.*


class MainActivity : BaseActivity(){

    override fun getLayoutRes(): Int {
        return R.layout.app_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.translucentStatusBar(this)
        toSplash()
    }

    private fun toSplash(){
        FragmentUtils.replace(supportFragmentManager,R.id.fragment_container,SplashFragment())
    }

    fun toHome(){
        FragmentUtils.replace(supportFragmentManager,R.id.fragment_container,HomeFragment())
    }
}

