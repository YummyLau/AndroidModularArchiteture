package com.effective.android.component.paccounts

import android.os.Bundle
import com.effective.android.base.util.FragmentUtils
import com.effective.android.debug.DebugActivity
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val paccountsSdk = SdkManager.getSdk(ComponentPaccountsSdk::class.java)
        FragmentUtils.replace(supportFragmentManager, R.id.id_fragment_container, paccountsSdk!!.getMainFragment(), "")
    }
}
