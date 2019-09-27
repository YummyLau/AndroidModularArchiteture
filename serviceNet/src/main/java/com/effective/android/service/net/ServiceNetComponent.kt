package com.effective.android.service.net

import android.app.Application
import com.effective.android.net.okhttp.HttpClient
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent


@AutoInjectComponent(impl = [ServiceNetImpl::class])
class ServiceNetComponent : IComponent {

    override fun detachComponent() {
        //暂时不处理
    }

    override fun attachComponent(application: Application) {
        HttpClient.init(application!!, true)
    }
}