package com.effective.android.video.setting

import android.app.Activity
import android.content.ContentResolver
import android.provider.Settings
import android.view.Window
import android.view.WindowManager

import androidx.annotation.IntDef
import androidx.annotation.IntRange


/**
 * 亮度管理
 * https://developer.android.com/reference/android/provider/Settings.System.html
 * 写入需要 <uses-permission android:name="android.permission.WRITE_SETTINGS"></uses-permission> 权限
 * 关于window亮度说明
 * [WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE]  默认使用System，当前window未覆盖
 * [WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF]   黑
 * [WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL]  亮
 * Created by g8931 on 2018/3/24.
 */

object BrightnessSetting {


    @IntDef(Mode.AUTOMATIC, Mode.MANUAL)
    annotation class Mode {
        companion object {
            val AUTOMATIC = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            val MANUAL = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        }
    }

    fun isScreenAutomaticMode(aContentResolver: ContentResolver): Boolean {
        var automatic = false
        try {
            automatic = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        return automatic
    }

    private fun setScreenBrightnessMode(activity: Activity, @Mode mode: Int) {
        val contentResolver = activity.contentResolver
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, mode)
    }

    fun startScreenBrightnessAutomatic(activity: Activity) {
        setScreenBrightnessMode(activity, Mode.AUTOMATIC)
    }

    fun startScreenBrightnessManual(activity: Activity) {
        setScreenBrightnessMode(activity, Mode.MANUAL)
    }

    @JvmOverloads
    fun getScreenBrightness(activity: Activity, @IntRange(from = 0, to = 255) def: Int = 125): Int {
        val contentResolver = activity.contentResolver
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, def)
    }

    fun setWindowBrightness(activity: Activity, @IntRange(from = 0, to = 255) brightness: Int) {
        val window = activity.window
        val lp = window.attributes
        lp.screenBrightness = brightness / 255.0f
        window.attributes = lp
    }

    fun setWindowBrightness(activity: Activity, brightness: Float) {
        var brightness = brightness
        val window = activity.window
        val lp = window.attributes
        if (brightness < 0) {
            brightness = 0f
        } else if (brightness > 1) {
            brightness = 1f
        }
        lp.screenBrightness = brightness
        window.attributes = lp
    }

    fun getWindowBrightness(activity: Activity): Float {
        val window = activity.window
        val lp = window.attributes
        return lp.screenBrightness
    }
}
