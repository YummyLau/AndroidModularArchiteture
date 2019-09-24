package com.effective.android.component.system

import android.os.Bundle

import com.effective.android.base.util.FragmentUtils
import com.effective.android.debug.DebugActivity
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val systemSdk = SdkManager.getSdk(ComponentSystemSdk::class.java)
        FragmentUtils.replace(supportFragmentManager, R.id.id_fragment_container, systemSdk!!.getMainFragment(), "")
    }
}
