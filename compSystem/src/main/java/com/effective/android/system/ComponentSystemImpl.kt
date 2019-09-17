package com.effective.android.system

import androidx.fragment.app.Fragment
import com.effective.android.component.system.ComponentSystemSdk
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentSystemSdk::class])
class ComponentSystemImpl : ComponentSystemSdk {

    override fun getMainFragment(): Fragment {
        return Fragment()
    }
}