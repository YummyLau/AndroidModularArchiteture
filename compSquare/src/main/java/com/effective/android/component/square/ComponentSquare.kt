package com.effective.android.component.square

import android.app.Application
import android.content.Context
import com.effective.android.base.rxjava.AppRxPlugins
import com.effective.android.webview.Utils
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

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