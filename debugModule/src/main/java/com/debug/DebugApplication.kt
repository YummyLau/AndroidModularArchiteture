package com.debug

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

open class DebugApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}