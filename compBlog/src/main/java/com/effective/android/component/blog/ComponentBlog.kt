package com.effective.android.component.blog

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ComponentBlogImpl::class])
class ComponentBlog : IComponent {

    override fun detachComponent() {
        //opt
    }

    override fun attachComponent(application: Application?) {
        //opt
    }
}