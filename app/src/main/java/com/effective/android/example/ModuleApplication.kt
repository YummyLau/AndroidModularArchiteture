package com.effective.android.example

import com.effective.android.base.BaseApplication
import com.effective.android.base.view.refreshlayout.SmartRefreshLayoutConfigure

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class ModuleApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        SmartRefreshLayoutConfigure.initConfigure(this)
        Sdks.init(this)
    }

}
