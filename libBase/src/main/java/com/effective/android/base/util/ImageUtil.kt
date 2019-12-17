package com.effective.android.base.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.effective.android.base.R
import java.io.ByteArrayOutputStream

/**
 * 图片相关的工具
 * bitmap，drawable等
 * created by yummylau 2019/12/17
 */
object ImageUtil {

    @JvmStatic
    fun zoomDrawable(drawable: Drawable, w: Int, h: Int): Drawable {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val oldbmp = drawableToBitmap(drawable)
        val matrix = Matrix()
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        val newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true)
        return BitmapDrawable(null, newbmp)
    }

    @JvmStatic
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    fun drawableToBitmap(context: Context, @DrawableRes drawableId: Int): Bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

    @JvmStatic
    fun bitmapToDrawable(context: Context, bitmap: Bitmap): Drawable = BitmapDrawable(context.resources, bitmap)

    @JvmStatic
    fun bitmapToBytes(context: Context, bitmap: Bitmap): ByteArray {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        return outStream.toByteArray()
    }

    @JvmStatic
    fun bytesToBitmap(byteArray: ByteArray): Bitmap? {
        return if (byteArray.isNotEmpty()) {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } else {
            null
        }
    }


}
