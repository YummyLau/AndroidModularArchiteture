package com.effective.android.base.util

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * 渠道工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
object ChannelUtils {

    private const val PROPS_CHANNEL_INFO = "channel/info"
    private const val PROP_CHANNEL_INFO_NAME = "name"
    private const val PROP_CHANNEL_PREFIX = "META-INF/mychannel"            //必须和jenkins打包写入的文件一样
    private const val CHANNEL_DEFAULT = "default"
    private const val CHANNEL_FILE = "channel_data"
    private var channel: String? = null

    fun fromMeta(context: Context): String? {
        try {
            return context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData.getString("CHANNEL")
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    fun fromMetaInf(context: Context?): String {
        if (context == null) return ""
        val appinfo = context.applicationInfo
        val sourceDir = appinfo.sourceDir
        var ret = ""
        var zipfile: ZipFile? = null
        try {
            zipfile = ZipFile(sourceDir)
            val entries = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName.startsWith(PROP_CHANNEL_PREFIX)) {
                    ret = entryName
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        val split = ret.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (split != null && split.size >= 2) {
            ret.substring(split[0].length + 1)
        } else {
            ""
        }
    }

    fun getChannel(context: Context): String {
        if (TextUtils.isEmpty(channel)) {
            channel = channelFromFile(context)
            if (TextUtils.isEmpty(channel)) {
                channel = fromMetaInf(context)
                if (TextUtils.isEmpty(channel)) {
                    channel = CHANNEL_DEFAULT
                }
                saveChannelToFile(context, channel!!)
            }
        }
        return channel!!
    }


    private fun channelFromFile(context: Context): String? {
        val path = StorageUtils.getChannelDir(context) + '/' + CHANNEL_FILE
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(path)
            return ObjectInputStream(inputStream).readObject() as String
        } catch (tr: Throwable) {
            tr.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (tr: Throwable) {
                    tr.printStackTrace()
                }

            }
        }

        return null
    }

    private fun saveChannelToFile(context: Context, channel: String): Boolean {
        val path = StorageUtils.getChannelDir(context)  + '/' + CHANNEL_FILE
        if (TextUtils.isEmpty(path)) {
            return false
        }
        var os: OutputStream? = null
        try {
            os = FileOutputStream(path)
            ObjectOutputStream(os).writeObject(channel)
            os!!.flush()
            return true
        } catch (tr: Throwable) {
            tr.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (tr: Throwable) {
                    tr.printStackTrace()
                }

            }
        }
        return false
    }

}