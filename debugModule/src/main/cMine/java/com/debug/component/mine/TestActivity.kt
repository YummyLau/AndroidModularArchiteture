package com.debug.component.mine

import android.os.Bundle

import com.effective.android.base.util.FragmentUtils
import com.debug.DebugActivity
import com.debug.R
import com.effective.android.component.mine.ComponentMineSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val componentMineSdk = SdkManager.getSdk(ComponentMineSdk::class.java)
        FragmentUtils.add(supportFragmentManager, R.id.id_fragment_container, componentMineSdk!!.getMainFragment(), "")
    }
}
