package com.effective.android.example

import android.app.Application
import com.effective.android.component.tab.home.ComponentTabHomeSdk
import com.effective.android.component.tab.mine.ComponentTabMineSdk
import com.effective.android.component.tab.recommendation.ComponentTabRecommendationSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager

class Sdks {

    companion object {
        lateinit var componentTabHomeSdk: ComponentTabHomeSdk
        lateinit var componentMineSdk: ComponentTabMineSdk
        lateinit var componentTabRecommendationSdk: ComponentTabRecommendationSdk

        fun init(application: Application) {
            ComponentManager.init(application)
            componentTabHomeSdk = SdkManager.getSdk(ComponentTabHomeSdk::class.java)!!
            componentMineSdk = SdkManager.getSdk(ComponentTabMineSdk::class.java)!!
            componentTabRecommendationSdk = SdkManager.getSdk(ComponentTabRecommendationSdk::class.java)!!
        }
    }
}