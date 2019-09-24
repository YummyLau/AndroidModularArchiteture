package com.effective.android.debug

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.effective.android.base.toast.ToastUtils

open class DebugApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}