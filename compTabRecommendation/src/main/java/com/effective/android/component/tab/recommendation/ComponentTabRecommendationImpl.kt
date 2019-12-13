package com.effective.android.component.tab.recommendation

import androidx.fragment.app.Fragment
import com.effective.android.component.tab.recommendation.view.TabRecommendationFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentTabRecommendationSdk::class])
class ComponentTabRecommendationImpl : ComponentTabRecommendationSdk{

    private var mainFragment: TabRecommendationFragment? = null

    override fun getMainFragment(): Fragment {
        if (mainFragment == null) {
            mainFragment = TabRecommendationFragment()
        }
        return mainFragment!!
    }
}