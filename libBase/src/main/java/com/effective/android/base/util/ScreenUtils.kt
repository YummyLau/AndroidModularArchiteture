package com.effective.android.base.util

import android.content.Context
import android.content.res.Resources
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics

import java.lang.reflect.Field

object ScreenUtils {

    private val RATIO = 0.85
    var initialScreenRatio: Double = 0.toDouble()
        private set

    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var screenMin: Int = 0// 宽高中，较小的值
    var screenMax: Int = 0// 宽高中，较大的值

    var density: Float = 0.toFloat()
    var scaleDensity: Float = 0.toFloat()
    var xdpi: Float = 0.toFloat()
    var ydpi: Float = 0.toFloat()
    var densityDpi: Int = 0

    var dialogWidth: Int = 0
    var statusbarheight: Int = 0
    var navbarheight: Int = 0
    var actionbarheight: Float = 0.toFloat()

    var oritation = -1

    fun init(context: Context?) {
        if (null == context) {
            return
        }
        val resources = context.applicationContext.resources
        oritation = resources.configuration.orientation
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        screenMin = if (screenWidth > screenHeight) screenHeight else screenWidth
        screenMax = if (screenWidth < screenHeight) screenHeight else screenWidth
        density = dm.density
        scaleDensity = dm.scaledDensity
        xdpi = dm.xdpi
        ydpi = dm.ydpi
        densityDpi = dm.densityDpi
        statusbarheight = getStatusBarHeight(context)
        navbarheight = getNavBarHeight(context)

        if (initialScreenRatio == 0.0) {
            initialScreenRatio = screenHeight * 1.0 / screenWidth
        }
    }

    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        if (statusbarheight == 0) {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val o = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field.get(o) as Int
                statusbarheight = context.resources
                        .getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (statusbarheight == 0) {
            statusbarheight = DisplayUtils.dip2px(context, 25f)
        }
        return statusbarheight
    }

    @JvmStatic
    fun getNavBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }


    @JvmStatic
    private fun getScaleDensity(context: Context): Float {
        if (scaleDensity == 0f) {
            init(context)
        }
        return scaleDensity
    }

    @JvmStatic
    private fun getDisplayDensity(context: Context): Float {
        if (density == 0f) {
            init(context)
        }
        return density
    }

    @JvmStatic
    fun getDisplayWidth(context: Context): Int {
        if (screenWidth == 0) {
            init(context)
        }
        return screenWidth
    }

    /**
     * 现在的数据是横屏时拿的还是竖屏拿的
     * 判断完可以决定是重新init还是自己颠倒宽高
     */
    @JvmStatic
    fun getOrientationOfData(context: Context): Int {
        init(context)
        return oritation
    }

    @JvmStatic
    fun getDisplayHeight(context: Context): Int {
        init(context)
        return screenHeight
    }

    @JvmStatic
    fun getScreenMin(context: Context): Int {
        if (screenMin == 0) {
            init(context)
        }
        return screenMin
    }

    @JvmStatic
    fun getScreenMax(context: Context): Int {
        if (screenMin == 0) {
            init(context)
        }
        return screenMax
    }

    @JvmStatic
    fun getDialogWidth(context: Context): Int {
        dialogWidth = (getScreenMin(context) * RATIO).toInt()
        return dialogWidth
    }

    @JvmStatic
    fun getActionbarheight(context: Context, action_bar_height: Int): Float {
        if (actionbarheight == 0f) {
            actionbarheight = context.resources
                    .getDimension(action_bar_height)
        }
        return actionbarheight

    }

    @JvmStatic
    fun getScreenHeight(context: Context): Float {
        var screenHeight = getDisplayHeight(context).toFloat()
        val screenWidth = getDisplayWidth(context).toFloat()
        if (screenWidth > screenHeight) {
            screenHeight = screenWidth
        }
        val statusBarHeight = ScreenUtils.statusbarheight.toFloat()
        val navHeight = navbarheight.toFloat()
        val deviceName = android.os.Build.MANUFACTURER
        if (!TextUtils.isEmpty(deviceName) && deviceName.toLowerCase()
                        .contains("meizu")) {
            val sbAutoHide = Settings.System.getInt(context.contentResolver, "mz_smartbar_auto_hide", 0)
            return if (sbAutoHide == 1) {
                screenHeight - statusBarHeight
            } else screenHeight - statusBarHeight - navHeight
        }
        return screenHeight - statusBarHeight
    }

}
