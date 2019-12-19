package com.effective.android.example.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.effective.android.base.activity.BaseActivity
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.base.util.FragmentUtils
import com.effective.android.base.util.ResourceUtils
import com.effective.android.example.R
import kotlinx.android.synthetic.main.app_activity_main_layout.*


class MainActivity : BaseActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.app_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.setStatusBarColor(this, ResourceUtils.getColor(this, R.color.transparent))
        toSplash()
    }

    private fun toSplash() {
        FragmentUtils.replace(supportFragmentManager, R.id.splashContainer, SplashFragment())
        FragmentUtils.replace(supportFragmentManager, R.id.mainContainer, MainFragment())
    }

    fun toHome() {
        splashContainer.visibility = View.GONE
        mainContainer.visibility = View.VISIBLE
    }
}

