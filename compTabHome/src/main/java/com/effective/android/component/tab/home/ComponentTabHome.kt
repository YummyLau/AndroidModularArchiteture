package com.effective.android.component.tab.home

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentTabHomeImpl::class])
class ComponentTabHome : IComponent{

    override fun attachComponent(application: Application) {
        Sdks.init(application)
    }

    override fun detachComponent() {

    }
}