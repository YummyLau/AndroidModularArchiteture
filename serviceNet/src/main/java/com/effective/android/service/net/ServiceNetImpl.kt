package com.effective.android.service.net

import com.effective.android.net.retrofit.RetrofitClient
import com.plugin.component.anno.AutoInjectImpl


@AutoInjectImpl(sdk = [ServiceNet::class])
class ServiceNetImpl : ServiceNet {

    override fun <T> service(baseUrl: String, type: Type, tClass: Class<T>): T {
        return RetrofitClient.instance.getService(baseUrl, type.ordinal, tClass)
    }
}