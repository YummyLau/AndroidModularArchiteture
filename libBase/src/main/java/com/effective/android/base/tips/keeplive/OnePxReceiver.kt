package com.effective.android.base.tips.keeplive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log

/**
 * 监听屏幕点亮和关闭
 * Created by yummyLau on 2018/7/19.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class OnePxReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = OnePxReceiver::class.java!!.simpleName
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        Log.e(TAG, "receive action:" + action!!)
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            OnePxManager.getInstance().startKeepLiveActivity(context!!)
        } else if (TextUtils.equals(action, Intent.ACTION_USER_PRESENT)) {
            OnePxManager.getInstance().finishKeepLiveActivity()
        }
    }
}