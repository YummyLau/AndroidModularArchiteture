package com.effective.android.component.blog.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.component.blog.R
import com.effective.android.component.blog.vm.BlogDetailViewModel
import com.effective.android.webview.Utils
import kotlinx.android.synthetic.main.blog_activity_blog_detail_layout.*

class BlogDetailActivity : BaseVmActivity<BlogDetailViewModel>() {

    companion object {
        private const val bundle_article: String = "bundle_article"

        fun startActivity(context: Context, article: String) {
            val intent = Intent(context, BlogDetailActivity::class.java)
            intent.putExtra(bundle_article, article)
            context.startActivity(intent)
        }
    }

    override fun getViewModel(): Class<BlogDetailViewModel> = BlogDetailViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.blog_activity_blog_detail_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.translucentStatusBar(this)
        val url: String = intent.getStringExtra(bundle_article)
        Utils.setDefaultWebViewSetting(webView)
        webView.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.destroyWebView(webView)
    }
}