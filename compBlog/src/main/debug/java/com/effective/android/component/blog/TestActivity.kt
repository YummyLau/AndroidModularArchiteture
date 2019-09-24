package com.effective.android.component.blog

import android.os.Bundle

import com.effective.android.base.util.FragmentUtils
import com.effective.android.debug.DebugActivity
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
