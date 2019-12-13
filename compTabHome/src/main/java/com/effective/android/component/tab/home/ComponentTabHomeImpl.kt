package com.effective.android.component.tab.home

import androidx.fragment.app.Fragment
import com.effective.android.component.tab.home.view.TabHomeFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentTabHomeSdk::class])
class ComponentTabHomeImpl : ComponentTabHomeSdk {

    private var mainFragment: TabHomeFragment? = null

    override fun getMainFragment(): Fragment {
        if (mainFragment == null) {
            mainFragment = TabHomeFragment()
        }
        return mainFragment!!
    }
}