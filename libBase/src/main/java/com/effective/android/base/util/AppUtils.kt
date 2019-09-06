package com.effective.android.base.util

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresPermission
import java.io.*
import java.util.*

/**
 * app信息
 * 包括版本，渠道等等,唯一标识符
 * 唯一标识符的最佳用法参考 https://developer.android.com/training/articles/user-data-ids.html
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/19.
 */
object AppUtils {

    private var imei: String? = null

    /**
     * 获取渠道信息
     * 先从 MetaInf 读取之后写入缓存文件
     */
    @JvmStatic
    fun getChannel(context: Context): String = ChannelUtils.getChannel(context)

    /**
     * 自定义全局唯一 ID (GUID)
     */
    @JvmStatic
    fun getUuid(context: Context): String {
        var uuid: UUID? = null
        val path = StorageUtils.getImportantDir(context) + "/uuid"
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(path)
            uuid = ObjectInputStream(inputStream).readObject() as UUID
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
        if (uuid == null) {
            uuid = UUID.randomUUID()
        }
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(path)
            ObjectOutputStream(outputStream).writeObject(uuid)
            outputStream.flush()
        } catch (tr: Throwable) {
            tr.printStackTrace()
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (tr: Throwable) {
                    tr.printStackTrace()
                }

            }
        }
        return if (uuid == null) {
            uuid.toString().replace("-".toRegex(), "")
        } else {
            ""
        }
    }

    @RequiresPermission(allOf = [READ_PHONE_STATE])
    @JvmStatic
    fun getDeviceId(ctx: Context): String? {
        if (imei == null) {
            val telman = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            try {
                //需要 android.permission.READ_PHONE_STATE 权限
                imei = telman.deviceId
            } catch (e: SecurityException) {
                imei = null
            }
        }
        return imei
    }

    @JvmStatic
    fun getAndroidId(context: Context) = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
}