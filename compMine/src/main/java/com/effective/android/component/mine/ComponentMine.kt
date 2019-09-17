package com.effective.android.component.mine

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentMineImpl::class])
class ComponentMine : IComponent {

    override fun detachComponent() {
        //opt
    }

    override fun attachComponent(application: Application?) {
        //opt
    }
}