package com.effective.android.component.tab.home

import android.app.Application
import com.effective.android.component.blog.ComponentBlogSdk
import com.effective.android.component.paccounts.ComponentPaccountsSdk
import com.effective.android.component.project.ComponentProjectSdk
import com.effective.android.component.system.ComponentSystemSdk
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager

class Sdks {

    companion object {
        lateinit var blogSdk: ComponentBlogSdk
        lateinit var paccountsSdk: ComponentPaccountsSdk
        lateinit var systemSdk: ComponentSystemSdk
        lateinit var projectSdk: ComponentProjectSdk

        fun init(application: Application) {
            ComponentManager.init(application)
            blogSdk = SdkManager.getSdk(ComponentBlogSdk::class.java)!!
            paccountsSdk = SdkManager.getSdk(ComponentPaccountsSdk::class.java)!!
            systemSdk = SdkManager.getSdk(ComponentSystemSdk::class.java)!!
            projectSdk = SdkManager.getSdk(ComponentProjectSdk::class.java)!!
        }
    }
}