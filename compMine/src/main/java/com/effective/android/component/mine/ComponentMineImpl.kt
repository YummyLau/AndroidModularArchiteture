package com.effective.android.component.mine

import androidx.fragment.app.Fragment
import com.plugin.component.anno.AutoInjectImpl


@AutoInjectImpl(sdk = [ComponentMineSdk::class])
class ComponentMineImpl : ComponentMineSdk {

    override fun getMainFragment(): Fragment {
        return Fragment()
    }
}