package com.effective.android.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.AbstractFlow

/**
 * 基础application，统一处理 65536
 * 5.0之前 dalvik 使用 multidex 对其他dex文件进行管理
 * 5.0及其更高版本，art支持从apk加载多个dex文件，在安装时执行预编译扫描 classN.dex文件编译成 .oat文件
 *
 * gradle添加
 * android {
 * defaultConfig {
 * ...
 * multiDexEnabled true
 * }
 *}
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
open class BaseApplication() : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}