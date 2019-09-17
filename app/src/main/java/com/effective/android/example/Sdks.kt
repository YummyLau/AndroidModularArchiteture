package com.effective.android.example

import android.app.Application
import com.effective.android.component.blog.ComponentBlogSdk
import com.effective.android.component.mine.ComponentMineSdk
import com.effective.android.component.paccounts.ComponentPaccountsSdk
import com.effective.android.component.project.ComponentProjectSdk
import com.effective.android.component.system.ComponentSystemSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager

class Sdks {

    companion object {
        lateinit var componentBlogSdk: ComponentBlogSdk
        lateinit var componentProjectSdk: ComponentProjectSdk
        lateinit var componentPaccountsSdk: ComponentPaccountsSdk
        lateinit var componentMineSdk: ComponentMineSdk
        lateinit var componentSystemSdk: ComponentSystemSdk

        fun init(application: Application) {
            ComponentManager.init(application)
            componentBlogSdk = SdkManager.getSdk(ComponentBlogSdk::class.java)!!
            componentProjectSdk = SdkManager.getSdk(ComponentProjectSdk::class.java)!!
            componentPaccountsSdk = SdkManager.getSdk(ComponentPaccountsSdk::class.java)!!
            componentMineSdk = SdkManager.getSdk(ComponentMineSdk::class.java)!!
            componentSystemSdk = SdkManager.getSdk(ComponentSystemSdk::class.java)!!
        }
    }
}