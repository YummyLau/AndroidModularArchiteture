package com.effective.android.base.tips.keeplive

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log

class OnePxService : Service() {

    private var receiver: OnePxReceiver? = null

    companion object {
        private val TAG = OnePxService::class.java.simpleName
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "start KeepLiveService !")
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        receiver = OnePxReceiver()
        registerReceiver(receiver, filter)
        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

}