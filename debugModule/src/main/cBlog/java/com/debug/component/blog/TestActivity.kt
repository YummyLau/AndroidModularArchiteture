package com.debug.component.blog

import android.os.Bundle
import com.debug.DebugActivity
import com.debug.R

import com.effective.android.base.util.FragmentUtils
import com.effective.android.component.blog.ComponentBlogSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val sdk = SdkManager.getSdk(ComponentBlogSdk::class.java)
        FragmentUtils.add(supportFragmentManager, R.id.id_fragment_container, sdk!!.getMainFragment(), "")
    }
}
