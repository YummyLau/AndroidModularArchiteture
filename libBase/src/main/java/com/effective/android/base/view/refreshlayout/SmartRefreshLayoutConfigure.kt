package com.effective.android.base.view.refreshlayout

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.effective.android.base.R
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.util.TimeFormatUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader


object SmartRefreshLayoutConfigure {

    @JvmStatic
    fun initConfigure(application: Application) {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        SmartRefreshLayout.setDefaultRefreshInitializer { context: Context?, layout: RefreshLayout ->
            layout.setEnableAutoLoadMore(true)
            layout.setEnableOverScrollDrag(false)
            layout.setEnableOverScrollBounce(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
            layout.setEnableScrollContentWhenRefreshed(true)
            layout.setPrimaryColors(ResourceUtils.getColor(context!!, R.color.windowBackground), ResourceUtils.getColor(context, R.color.colorTextPrimary))
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, layout: RefreshLayout? -> ClassicsHeader(context).setTimeFormat(TimeFormatUtils("更新于 %s")) }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).setDrawableSize(20f).setPrimaryColor(ResourceUtils.getColor(context, R.color.windowBackground)).setAccentColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        }
    }

    @JvmStatic
    fun refreshRefreshTheme(context: Context, layout: RefreshLayout?) {
        SmartRefreshLayout.setDefaultRefreshInitializer { context: Context?, layout: RefreshLayout ->
            //全局设置（优先级最低）
            layout.setEnableAutoLoadMore(true)
            layout.setEnableOverScrollDrag(false)
            layout.setEnableOverScrollBounce(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
            layout.setEnableScrollContentWhenRefreshed(true)
            layout.setPrimaryColors(ResourceUtils.getColor(context!!, R.color.windowBackground), ResourceUtils.getColor(context, R.color.colorTextPrimary))
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).setDrawableSize(20f).setPrimaryColor(ResourceUtils.getColor(context, R.color.windowBackground)).setAccentColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        }
        (layout?.refreshFooter as ClassicsFooter).setPrimaryColor(ResourceUtils.getColor(context, R.color.windowBackground)).setAccentColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        layout.setPrimaryColors(ResourceUtils.getColor(context, R.color.windowBackground), ResourceUtils.getColor(context, R.color.colorTextPrimary))
    }
}