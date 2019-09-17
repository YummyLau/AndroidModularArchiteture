package com.effective.android.system

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentSystemImpl::class])
class ComponentSystem : IComponent {

    override fun detachComponent() {
        //opt
    }

    override fun attachComponent(application: Application?) {
        //opt
    }
}