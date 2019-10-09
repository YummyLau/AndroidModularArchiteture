package com.debug.component.project

import android.os.Bundle
import com.effective.android.base.util.FragmentUtils
import com.debug.DebugActivity
import com.debug.R
import com.effective.android.component.project.ComponentProjectSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val projectSdk = SdkManager.getSdk(ComponentProjectSdk::class.java)
        FragmentUtils.replace(supportFragmentManager, R.id.id_fragment_container, projectSdk!!.getMainFragment(), "")
    }
}
