package com.effective.android.component.tab.recommendation.view

import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.vm.TabRecommendationVm

class TabRecommendationFragment : BaseVmFragment<TabRecommendationVm>() {

    override fun getViewModel(): Class<TabRecommendationVm> = TabRecommendationVm::class.java

    override fun getLayoutRes(): Int  = R.layout.tabr_fragment_recommendation_layout
}