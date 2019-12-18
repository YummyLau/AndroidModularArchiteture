package com.effective.android.component.square.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.base.systemui.QMUIStatusBarHelper
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.R
import com.effective.android.component.square.bean.Article
import com.effective.android.component.square.vm.BlogDetailViewModel
import com.effective.android.webview.Utils
import kotlinx.android.synthetic.main.square_activity_blog_detail_layout.*

class BlogDetailActivity : BaseVmActivity<BlogDetailViewModel>() {

    companion object {
        private const val bundle_model: String = "bundle_by_article"
        private const val bundle_article: String = "bundle_article"
        private const val bundle_url: String = "bundle_url"

        fun startActivity(context: Context, article: Article) {
            val intent = Intent(context, BlogDetailActivity::class.java)
            intent.putExtra(bundle_article, article)
            intent.putExtra(bundle_model, true)
            context.startActivity(intent)
        }

        fun startActivity(context: Context, string: String) {
            val intent = Intent(context, BlogDetailActivity::class.java)
            intent.putExtra(bundle_url, string)
            intent.putExtra(bundle_model, false)
            context.startActivity(intent)
        }
    }

    override fun getViewModel(): Class<BlogDetailViewModel> = BlogDetailViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.square_activity_blog_detail_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.setStatusBarColor(this, ResourceUtils.getColor(this, R.color.windowBackground))
        QMUIStatusBarHelper.setStatusBarLightMode(this)
        Utils.setDefaultWebViewSetting(webView)
        val byArticle: Boolean = intent.getBooleanExtra(bundle_model, false)
        if (byArticle) {
            val article: Article = intent.getParcelableExtra(bundle_article)
            webView.loadUrl(article.link)
            if (!TextUtils.isEmpty(article.title)) {
                titleLayout.title.text = article.title
            }
        } else {
            val url: String = intent.getStringExtra(bundle_url)
            webView.loadUrl(url)
        }
        titleLayout.leftAction.setOnClickListener {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Utils.destroyWebView(webView)
    }
}