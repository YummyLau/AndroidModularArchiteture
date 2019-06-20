package com.effective.android.base.view.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * 可播放gif的span
 * Created by yummyLau on 2018/7/02.
 * Email: yummyl.lau@gmail.com
 */

class GifImageSpan : ImageSpan {

    private var mDrawable: Drawable? = null

    constructor(d: Drawable) : super(d) {
        mDrawable = d
    }

    constructor(d: Drawable, verticalAlignment: Int) : super(d, verticalAlignment) {
        mDrawable = d
    }

    override fun getDrawable(): Drawable? {
        return mDrawable
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int,
                         fontMetricsInt: Paint.FontMetricsInt?): Int {
        val drawable = drawable
        val rect = drawable!!.bounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top

            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4

            fontMetricsInt.ascent = -bottom
            fontMetricsInt.top = -bottom
            fontMetricsInt.bottom = top
            fontMetricsInt.descent = top
        }
        return rect.right
    }

    /**
     * @param canvas
     * @param text
     * @param start
     * @param end
     * @param x      要绘制的image的左边框到textview左边框的距离。
     * @param top    替换行的最顶部位置
     * @param y      要替换的文字的基线坐标，即基线到textview上边框的距离
     * @param bottom 替换行的最底部位置。注意，textview中两行之间的行间距是属于上一行的，所以这里bottom是指行间隔的底部位置
     * @param paint
     */
    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int,
                      x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        //要绘制的image
        val drawable = drawable
        // font metrics of text to be replaced
        val fm = paint.fontMetricsInt
        val transY = (y + fm.descent + y + fm.ascent) / 2 - drawable!!.bounds.bottom / 2
        canvas.save()
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}
