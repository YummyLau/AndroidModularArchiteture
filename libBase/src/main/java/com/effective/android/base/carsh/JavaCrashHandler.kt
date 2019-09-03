package com.effective.android.base.carsh

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.effective.android.base.util.StorageUtils
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 捕获java层异常
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
class JavaCrashHandler(context: Context) : Thread.UncaughtExceptionHandler {

    private val mContext = context
    private val mInfoMap = HashMap<String, String>()
    private val mDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (handleException(t, e)) {
            return
        }
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e)
            return
        }
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }


    private fun handleException(t: Thread?, e: Throwable?): Boolean {
        if (e == null) {
            return false
        }
        //toast提示
        object : Thread() {
            override fun run() {
                Looper.prepare()
                try {
                    Toast.makeText(mContext, "应用异常退出!", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Looper.loop()
            }
        }.start()
        collectDeviceInfo(mContext)
        saveCrashInfo2File(e)
        return true
    }

    private fun collectDeviceInfo(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                mInfoMap.put("VersionName", versionName)
                mInfoMap.put("VersionCode", versionCode)
            }
            val timestamp = System.currentTimeMillis()
            val time = mDateFormat.format(Date())
            mInfoMap.put("CrashTime", time + "-" + timestamp)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                mInfoMap.put(field.name, field.get(null).toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {

        val sb = StringBuffer()
        for ((key, value) in mInfoMap) {
            sb.append(key).append("=").append(value).append("\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        Log.e("JavaCrashHandler", result)
        sb.append(result)
        try {
            val time = mDateFormat.format(Date())
            val fileName = "$time.txt"
            val crashCachePath = StorageUtils.getCrashDir(mContext)
            val fos = FileOutputStream("$crashCachePath/$fileName")
            fos.write(sb.toString().toByteArray())
            fos.close()
            pushCrash2Server(sb.toString())
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun pushCrash2Server(logString: String) {
        //todo  2019/06/19 通过websocket 上传到服务端
    }
}
