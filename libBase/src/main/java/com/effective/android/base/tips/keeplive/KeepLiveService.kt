package com.effective.android.base.tips.keeplive

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

/**
 * 方案二
 * 8.0以下尝试用前台服务降低优先级，但是7.0以下notification才不会提示，7.0会在通知栏显示isRunning
 * 模范tinker实现
 * Created by yummyLau on 2018/7/19.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class KeepLiveService : Service() {
    companion object {
        private val TAG = KeepLiveService::class.java.simpleName
        private val notificationId = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result =  super.onStartCommand(intent, flags, startId)
        if (Build.VERSION.SDK_INT >= 26) {
            Log.i(TAG, "for system version >= Android O, we just ignore increasingPriority " + "job to avoid crash or toasts.")
            return result
        }

        if ("ZUK" == Build.MANUFACTURER) {
            Log.i(TAG, "for ZUK device, we just ignore increasingPriority " + "job to avoid crash.")
            return result
        }

        Log.i(TAG, "try to increase process priority")

        try {
            val notification = Notification()
            if (Build.VERSION.SDK_INT < 18) {
                startForeground(notificationId, notification)
            } else {
                startForeground(notificationId, notification)
                startService(Intent(this, InnerService::class.java))
            }
        } catch (e: Throwable) {
            Log.i(TAG, "try to increase patch process priority error:$e")
        }


        return result
    }

    override fun onBind(intent: Intent?): IBinder? = null


    /**
     * start a service to keep alive!
     */
    class InnerService : Service() {

        override fun onCreate() {
            super.onCreate()
            try {
                startForeground(notificationId, Notification())
            } catch (e: Throwable) {
                Log.e(TAG, "InnerService set service for push exception:%s.", e)
            }

            // kill
            stopSelf()
        }

        override fun onDestroy() {
            stopForeground(true)
            super.onDestroy()
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }
    }
}