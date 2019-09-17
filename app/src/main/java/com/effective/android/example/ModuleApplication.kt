package com.effective.android.example

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class ModuleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Sdks.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
