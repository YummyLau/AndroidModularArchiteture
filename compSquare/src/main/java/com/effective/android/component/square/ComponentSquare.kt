package com.effective.android.component.square

import android.app.Application
import com.effective.android.base.rxjava.AppRxPlugins
import com.effective.android.webview.Utils
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentBlogImpl::class])
class ComponentSquare : IComponent {

    override fun detachComponent() {
    }

    override fun attachComponent(application: Application) {
        Sdks.init(application)
        AppRxPlugins.init()
        Utils.initX5Webkit(application)
    }
}