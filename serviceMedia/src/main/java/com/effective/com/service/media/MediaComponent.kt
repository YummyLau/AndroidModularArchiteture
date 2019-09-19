package com.effective.com.service.media

import android.app.Application
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent

@AutoInjectComponent(impl = [ImageloaderImpl::class, VideoloaderImpl::class])
class MediaComponent : IComponent {

    override fun detachComponent() {
    }

    override fun attachComponent(application: Application?) {
    }
}