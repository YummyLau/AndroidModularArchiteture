package com.effective.android.component.square

import android.app.Application
import com.effective.android.service.media.ServiceImageloader
import com.effective.android.service.net.ServiceNet
import com.effective.android.service.skin.ServiceSkin
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager

class Sdks {

    companion object {
        lateinit var serviceSkin: ServiceSkin
        lateinit var serviceNet: ServiceNet
        lateinit var serviceImageloder: ServiceImageloader
        private lateinit var componentBlogSdk: ComponentSquareSdk

        fun init(application: Application) {
            ComponentManager.init(application)
            serviceSkin = SdkManager.getSdk(ServiceSkin::class.java)!!
            serviceNet = SdkManager.getSdk(ServiceNet::class.java)!!
            serviceImageloder = SdkManager.getSdk(ServiceImageloader::class.java)!!
        }

        fun getSdk(): ComponentSquareSdk {
            if (!Companion::componentBlogSdk.isInitialized) {
                componentBlogSdk = SdkManager.getSdk(ComponentSquareSdk::class.java)!!
            }
            return componentBlogSdk
        }
    }


}