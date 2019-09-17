package com.effective.android.component.paccounts

import androidx.fragment.app.Fragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentPaccountsSdk::class])
class ComponentPaccountsImpl:ComponentPaccountsSdk {

    override fun getMainFragment(): Fragment {
        return Fragment()
    }
}