package com.effective.android.component.system

import androidx.fragment.app.Fragment
import com.effective.android.component.system.view.SystemFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentSystemSdk::class])
class ComponentSystemImpl : ComponentSystemSdk {

    private var systemFragment: SystemFragment? = null

    override fun getMainFragment(): Fragment {
        if (systemFragment == null) {
            systemFragment = SystemFragment()
        }
        return systemFragment!!
    }
}