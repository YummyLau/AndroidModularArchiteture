package com.effective.android.base.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File

/**
 * 目录管理，一般每个app都需要一些目录用于存放特定内容，比如缓存，data，下载内容，图片等
 * 需要新增哪些目录直接在这里添加就可以了。
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
object StorageUtils {

    private const val DATA_DATA = "/data/data"
    private const val DATA_CACHE = "cache"
    private const val CACHE = "cache"
    private const val LIB = "lib"
    private const val DEX = "dex"

    private const val TEMP_DIR = "temp"     //临时目录
    private const val APP_STORAGE_DIR = "/myApp/"  //根据不同的项目修改 myApp的命名

    const val APP_STORAGE_CRASH = "crash"     //crash 目录

    /**
     * sd卡是否挂载
     */
    fun sdcardEnable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()
    }

    fun storagePath(path: String = ""): String {
        var path = path
        if (TextUtils.isEmpty(path)) {
            path = TEMP_DIR
        }
        val root = if (sdcardEnable())
            Environment.getExternalStorageDirectory().absolutePath + APP_STORAGE_DIR
        else
            Environment.getDataDirectory().absolutePath
        val dir = File("$root/$path")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.absolutePath + "/"
    }

    fun storageFile(path: String = ""): File = File(storagePath(path))

    fun dataDir(context: Context): String {
        var dataDir = context.applicationInfo.dataDir
        if (TextUtils.isEmpty(dataDir)) {
            dataDir = DATA_DATA + "/" + context.packageName
        }
        return dataDir!!
    }

    fun libDir(context: Context): String {
        var libDir = ""
        if (Build.VERSION.SDK_INT >= 9) {
            libDir = context.applicationInfo.nativeLibraryDir
        } else {
            try {
                libDir = context.applicationInfo.dataDir + "/lib"
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return libDir!!
    }

    fun cacheDir(context: Context): String {
        var cacheDir = ""
        if (context.cacheDir == null) {
            cacheDir = DATA_DATA + "/" + context.packageName + "/" + DATA_CACHE
            // ENSURE
            val file = File(cacheDir)
            if (!file.exists()) {
                file.mkdir()
            }
        } else {
            cacheDir = context.cacheDir.absolutePath
        }
        return cacheDir!!
    }

    fun appLibDir(context: Context): String = context.getDir(LIB, Context.MODE_PRIVATE).absolutePath

    fun appCacheDir(context: Context): String = context.getDir(CACHE, Context.MODE_PRIVATE).absolutePath

    fun appDexDir(context: Context): String = context.getDir(DEX, Context.MODE_PRIVATE).absolutePath
}