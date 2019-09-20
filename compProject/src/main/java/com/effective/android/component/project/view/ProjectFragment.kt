package com.effective.android.component.project.view

import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.project.R
import com.effective.android.component.project.vm.ProjectViewModel

class ProjectFragment : BaseVmFragment<ProjectViewModel>() {

    override fun getViewModel(): Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_laout
}