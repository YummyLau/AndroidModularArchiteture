package com.effective.android.component.project

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentProjectImpl::class])
class ComponentProject : IComponent {

    override fun detachComponent() {
        //opt
    }

    override fun attachComponent(application: Application) {
        Sdks.init(application)
    }
}