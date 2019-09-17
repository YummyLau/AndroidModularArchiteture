package com.effective.android.component.project

import androidx.fragment.app.Fragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentProjectSdk::class])
class ComponentProjectImpl : ComponentProjectSdk {

    override fun getMainFragment(): Fragment {
        return Fragment()
    }
}