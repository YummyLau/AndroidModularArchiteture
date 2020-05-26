package com.debug.component.recomendation

import android.os.Bundle
import com.effective.android.base.util.FragmentUtils
import com.debug.DebugActivity
import com.debug.R
import com.effective.android.component.tab.recommendation.ComponentTabRecommendationSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.init(application)
        val recommendationSdk = SdkManager.getSdk(ComponentTabRecommendationSdk::class.java)
        FragmentUtils.replace(supportFragmentManager, R.id.id_fragment_container, recommendationSdk!!.getMainFragment(), "")
    }
}
