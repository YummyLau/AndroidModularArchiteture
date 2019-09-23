package com.effective.android.component.paccounts

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentPaccountsImpl::class])
class ComponentPaccounts : IComponent {

    override fun detachComponent() {
        //opt
    }

    override fun attachComponent(application: Application) {
        Sdks.init(application)
    }
}