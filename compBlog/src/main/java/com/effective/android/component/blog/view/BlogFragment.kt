package com.effective.android.component.blog.view

import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.blog.R
import com.effective.android.component.blog.vm.BlogViewModel

class BlogFragment : BaseVmFragment<BlogViewModel>() {

    override fun getViewModel(): Class<BlogViewModel> = BlogViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.cblog_main_fragment_layout
}