package com.effective.android.base.view.refreshlayout

import android.app.Application
import android.R.color
import android.graphics.Color
import androidx.annotation.ColorRes
import com.effective.android.base.R
import com.effective.android.base.util.ResourceUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader


object SmartRefreshLayoutConfigure {

    @JvmStatic
    fun init(application: Application) {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//            layout.setPrimaryColors(Color.BLACK,Color.WHITE)
            ClassicsHeader(context).setPrimaryColor(Color.BLACK).setAccentColor(Color.WHITE)
            //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f).setPrimaryColor(Color.BLACK).setAccentColor(Color.WHITE)
        }
    }
}