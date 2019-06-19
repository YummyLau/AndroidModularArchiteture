package com.effective.android.base.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

/**
 * 设备相关的工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
class DeviceUtils {

    var mTotalMemory = -1L

    /**
     * 是否已经 root 过
     */
    fun isRooted(context: Context): Boolean {
        val isSdk = isGoogleSdk(context)
        val tags = Build.TAGS
        if (!isSdk && tags != null
                && (tags as String).contains("test-keys")) {
            return true
        }
        if (File("/system/app/Superuser.apk").exists()) {
            return true
        }
        return !isSdk && File("/system/xbin/su").exists()
    }

    private fun isGoogleSdk(context: Context): Boolean {
        val str = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return "sdk" == Build.PRODUCT ||
                "google_sdk" == Build.PRODUCT ||
                str == null
    }

    /**
     * 获取电量信息
     */
    fun battery(context: Context): String {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent = context.registerReceiver(null, filter)
        val level = intent!!.getIntExtra("level", -1)
        val scale = intent.getIntExtra("scale", -1)
        return if (scale == -1) {
            "--"
        } else {
            String.format(Locale.US, "%d %%", level * 100 / scale)
        }
    }

    /**
     * 获取磁盘信息
     */
    fun disk(): String {
        val info = getSdCardMemory()
        val total = info[0]
        val avail = info[1]
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total))
        }
    }

    /**
     * 获取内存信息
     */
    private fun ram(context: Context): String {
        val total = getTotalMemory()
        val avail = getAvailableMemory(context)
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total))
        }
    }


    private fun getSdCardMemory(): LongArray {
        val sdCardInfo = LongArray(2)
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            if (Build.VERSION.SDK_INT >= 18) {
                val bSize = sf.blockSizeLong
                val bCount = sf.blockCountLong
                val availBlocks = sf.availableBlocksLong
                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            } else {
                val bSize = sf.blockSize.toLong()
                val bCount = sf.blockCount.toLong()
                val availBlocks = sf.availableBlocks.toLong()
                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            }
        }
        return sdCardInfo
    }

    private fun getSizeWithUnit(size: Long): String {
        return when (size) {
            in 1048576..1073741824 -> {
                val i = (size / 1048576).toFloat()
                String.format(Locale.US, "%.02f MB", i)
            }
            else -> {
                if (size >= 1073741824) {
                    val i = (size / 1073741824).toFloat()
                    String.format(Locale.US, "%.02f GB", i)
                } else {
                    val i = (size / 1024).toFloat()
                    String.format(Locale.US, "%.02f KB", i)
                }
            }
        }
    }

    /**
     * 获取可使用的内存
     */
    fun getAvailableMemory(context: Context): Long {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return mi.availMem
    }

    /**
     * 获取设备总内存
     */
    @Synchronized
    fun getTotalMemory(): Long {
        if (mTotalMemory == -1L) {
            var total = 0L
            var str: String?
            try {
                str = filterStringFromFile(File("/proc/meminfo"), "MemTotal")
                if (!TextUtils.isEmpty(str)) {
                    str = str!!.toUpperCase(Locale.US)
                    if (str.endsWith("KB")) {
                        total = getSize(str, "KB", 1024)
                    } else if (str.endsWith("MB")) {
                        total = getSize(str, "MB", 1048576)
                    } else if (str.endsWith("GB")) {
                        total = getSize(str, "GB", 1073741824)
                    } else {
                        total = -1
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mTotalMemory = total
        }
        return mTotalMemory
    }

    private fun filterStringFromFile(file: File, filter: String): String? {
        var str: String? = null
        if (file.exists()) {
            var br: BufferedReader? = null
            try {
                br = BufferedReader(FileReader(file), 1024)
                var line = br.readLine()
                while (line != null) {
                    val pattern = Pattern.compile("\\s*:\\s*")
                    val ret = pattern.split(line, 2)
                    if (ret != null && ret.size > 1 && ret[0] == filter) {
                        str = ret[1]
                        break
                    }
                    br.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    br!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return str
    }

    private fun getSize(size: String, uint: String, factor: Int): Long {
        return java.lang.Long.parseLong(size.split(uint.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }) * factor
    }
}