package com.effective.android.component.tab.mine

import android.app.Application
import com.effective.android.service.account.AccountSdk
import com.effective.android.service.media.ServiceImageloader
import com.effective.android.service.net.ServiceNet
import com.effective.android.service.skin.ServiceSkin
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager

class Sdks{
    companion object {

        lateinit var serviceSkin: ServiceSkin
        lateinit var serviceNet: ServiceNet
        lateinit var serviceAccount: AccountSdk
        lateinit var serviceImageloader: ServiceImageloader


        fun init(application: Application) {
            ComponentManager.init(application)
            serviceSkin = SdkManager.getSdk(ServiceSkin::class.java)!!
            serviceNet = SdkManager.getSdk(ServiceNet::class.java)!!
            serviceImageloader = SdkManager.getSdk(ServiceImageloader::class.java)!!
            serviceAccount = SdkManager.getSdk(AccountSdk::class.java)!!
        }
    }
}
