package com.effective.android.component.project.view

import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.project.R
import com.effective.android.component.project.vm.ArticleViewModel

class ArticleFragment : BaseVmFragment<ArticleViewModel>() {

    override fun getViewModel(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.project_fragment_article_layout
}