package com.effective.android.component.blog

import android.app.Application
import com.effective.android.base.rxjava.AppRxPlugins
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent
import io.reactivex.android.plugins.RxAndroidPlugins

@AutoInjectComponent(impl = [ComponentBlogImpl::class])
class ComponentBlog : IComponent {

    override fun detachComponent() {
    }

    override fun attachComponent(application: Application) {
        Sdks.init(application)
        AppRxPlugins.init()
    }
}