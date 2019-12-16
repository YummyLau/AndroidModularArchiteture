package com.effective.android.component.tab.recommendation

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentTabRecommendationImpl::class])
class ComponentTabRecommendation : IComponent{

    override fun attachComponent(application: Application) {
        Sdks.init(application)
    }

    override fun detachComponent() {

    }
}