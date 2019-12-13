package com.effective.android.component.tab.mine

import androidx.fragment.app.Fragment
import com.effective.android.component.tab.mine.view.MineFragment
import com.plugin.component.anno.AutoInjectImpl


@AutoInjectImpl(sdk = [ComponentTabMineSdk::class])
class ComponentMineImpl : ComponentTabMineSdk {

    private var mineFragment: MineFragment? = null

    override fun getMainFragment(): Fragment {
        if (mineFragment == null) {
            mineFragment = MineFragment()
        }
        return mineFragment!!
    }
}