package com.effective.android.base.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object ResourceUtils {

    @JvmStatic
    fun getColor(context: Context, @ColorRes color: Int) = ContextCompat.getColor(context, color)

    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes drawable: Int) = ContextCompat.getDrawable(context, drawable)

    @JvmStatic
    fun getDrawable(context: Context, name: String) = ContextCompat.getDrawable(context, context.resources.getIdentifier(name, "drawable", context.applicationInfo.packageName))

    @JvmStatic
    fun getString(context: Context, @StringRes string: Int) = context.getString(string)

}