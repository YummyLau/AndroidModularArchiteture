package com.effective.android.imageloader

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.ImageView

object Utils {

    @JvmStatic
    fun isValidContextForGlide(imageView: ImageView?): Context? {
        if (imageView == null || imageView.context == null) {
            return null
        }
        var activity = content2Activity(imageView.context)
        return if (activity != null && (activity.isDestroyed || activity.isFinishing)) {
            null
        } else {
            imageView.context
        }
    }

    private fun content2Activity(context: Context?): Activity? {
        return when (context) {
            null -> null
            is Activity -> context
            is ContextWrapper -> content2Activity(context.baseContext)
            else -> null
        }
    }
}