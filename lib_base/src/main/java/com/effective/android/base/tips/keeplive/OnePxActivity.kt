package com.effective.android.base.tips.keeplive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity

/**
 * 方案三，qq方案
 * 1像素activity
 * Created by yummyLau on 2018/7/19.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class OnePxActivity : AppCompatActivity() {

    companion object {
        private val TAG = OnePxActivity::class.java!!.getSimpleName()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "start Keep app activity")
        window.setGravity(Gravity.START or Gravity.TOP)
        val attributes = window.attributes
        attributes.width = 1
        attributes.height = 1
        attributes.x = 0
        attributes.y = 0
        window.attributes = attributes
        OnePxManager.getInstance().setKeep(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "stop keep app activity")
    }

}