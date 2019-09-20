package com.effective.android.component.project

import androidx.fragment.app.Fragment
import com.effective.android.component.project.view.ProjectFragment
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ComponentProjectSdk::class])
class ComponentProjectImpl : ComponentProjectSdk {

    private var mainFragment: ProjectFragment? = null

    override fun getMainFragment(): Fragment {
        if (mainFragment == null) {
            mainFragment = ProjectFragment()
        }
        return mainFragment!!
    }
}