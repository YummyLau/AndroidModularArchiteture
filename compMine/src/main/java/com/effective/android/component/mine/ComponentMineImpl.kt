package com.effective.android.component.mine

import androidx.fragment.app.Fragment
import com.effective.android.component.mine.view.MineFragment
import com.plugin.component.anno.AutoInjectImpl


@AutoInjectImpl(sdk = [ComponentMineSdk::class])
class ComponentMineImpl : ComponentMineSdk {

    private var mineFragment: MineFragment? = null

    override fun getMainFragment(): Fragment {
        if (mineFragment == null) {
            mineFragment = MineFragment()
        }
        return mineFragment!!
    }
}