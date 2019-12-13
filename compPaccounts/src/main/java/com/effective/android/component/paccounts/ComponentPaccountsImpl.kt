package com.effective.android.component.paccounts

import androidx.fragment.app.Fragment
import com.effective.android.component.paccounts.view.PaccountsFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentPaccountsSdk::class])
class ComponentPaccountsImpl : ComponentPaccountsSdk {

    private var paccountsFragment: PaccountsFragment? = null

    override fun getMainFragment(): Fragment {
        if (paccountsFragment == null) {
            paccountsFragment = PaccountsFragment()
        }
        return paccountsFragment!!
    }

    override fun getMainName(): String = "公众号"
}