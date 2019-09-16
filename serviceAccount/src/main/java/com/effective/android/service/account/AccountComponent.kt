package com.effective.android.service.account

import android.app.Application
import com.effective.android.service.account.data.AccountRepository
import com.effective.android.service.net.ServiceNet
import com.effective.android.service.skin.ServiceSkin
import com.plugin.component.IComponent
import com.plugin.component.SdkManager
import com.plugin.component.anno.AutoInjectComponent


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectComponent(impl = [AccountServiceImpl::class])
class AccountComponent : IComponent {

    companion object {
        lateinit var accountRepository: AccountRepository
        lateinit var accountServiceImpl: AccountServiceImpl
        lateinit var serviceSkin: ServiceSkin
        lateinit var serviceNet: ServiceNet
    }

    override fun attachComponent(application: Application) {
        accountRepository = AccountRepository(application)
        serviceSkin = SdkManager.getSdk(ServiceSkin::class.java)!!
        serviceNet = SdkManager.getSdk(ServiceNet::class.java)!!
    }

    override fun detachComponent() {

    }
}
