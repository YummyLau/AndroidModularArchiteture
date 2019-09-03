package com.effective.android.base.tips.keeplive

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.lang.ref.WeakReference

/**
 * Created by yummyLau on 2018/7/19.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class OnePxManager {

    @Volatile
    private var mKeepAct: WeakReference<Activity>? = null

    companion object {

        private var sInstance: OnePxManager? = null

        fun getInstance(): OnePxManager {
            if (sInstance == null) {
                synchronized(OnePxManager::class.java) {
                    if (sInstance == null) {
                        sInstance = OnePxManager()
                    }
                }
            }
            return sInstance!!
        }

    }


    fun startKeepLiveActivity(context: Context) {
        val intent = Intent(context, OnePxManager::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun finishKeepLiveActivity() {
        if (null != mKeepAct) {
            val activity = mKeepAct!!.get()
            activity?.finish()
            mKeepAct = null
        }
    }

    fun setKeep(keep: OnePxActivity) {
        mKeepAct = WeakReference(keep)
    }

}