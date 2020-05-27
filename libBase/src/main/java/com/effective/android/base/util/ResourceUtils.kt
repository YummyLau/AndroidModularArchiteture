package com.effective.android.base.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object ResourceUtils {

    private val inner = object : ResourceHandler {

        override fun getColor(context: Context, @ColorRes color: Int) = proxy?.getColor(context, color)
                ?: ContextCompat.getColor(context, color)

        override fun getDrawable(context: Context, @DrawableRes drawable: Int) = proxy?.getDrawable(context, drawable)
                ?: ContextCompat.getDrawable(context, drawable)

        override fun getDrawable(context: Context, name: String) = proxy?.getDrawable(context, name)
                ?: ContextCompat.getDrawable(context, context.resources.getIdentifier(name, "drawable", context.applicationInfo.packageName))

        override fun getString(context: Context, @StringRes string: Int): String = proxy?.getString(context, string)
                ?: context.getString(string)

        override fun getText(context: Context, @StringRes string: Int): CharSequence = proxy?.getText(context, string)
                ?: context.getText(string)

        override fun formatString(context: Context, @StringRes string: Int, targetSting: String): String = proxy?.formatString(context, string, targetSting)
                ?: String.format(context.getString(string), targetSting.replace("%", "%%", false))
    }

    private var proxy: ResourceHandler? = null

    @JvmStatic
    fun setResourceProxy(proxy: ResourceHandler) {
        this.proxy = proxy
    }

    @JvmStatic
    fun getColor(context: Context, @ColorRes color: Int) = inner.getColor(context, color)

    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes drawable: Int) = inner.getDrawable(context, drawable)

    @JvmStatic
    fun getDrawable(context: Context, name: String) = inner.getDrawable(context, name)

    @JvmStatic
    fun getString(context: Context, @StringRes string: Int): String = inner.getString(context, string)

    @JvmStatic
    fun getText(context: Context, @StringRes string: Int): CharSequence = inner.getText(context, string)

    @JvmStatic
    fun formatString(context: Context, @StringRes string: Int, targetSting: String): String = inner.formatString(context, string, targetSting)
}

interface ResourceHandler {

    @ColorInt
    fun getColor(context: Context, @ColorRes color: Int): Int

    fun getDrawable(context: Context, @DrawableRes drawable: Int): Drawable?

    fun getDrawable(context: Context, name: String): Drawable?

    fun getString(context: Context, @StringRes string: Int): String

    fun getText(context: Context, @StringRes string: Int): CharSequence

    fun formatString(context: Context, @StringRes string: Int, targetSting: String): String
}