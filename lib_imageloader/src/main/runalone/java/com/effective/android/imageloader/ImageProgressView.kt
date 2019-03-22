package com.effective.android.imageloader

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import jp.wasabeef.example.glide.px

class ImageProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs) {

    private val progressPaint: Paint
    private val outlinePaint: Paint
    private val progressRect: RectF
    private var progress = 0f

    init {
        progressPaint = Paint().apply {
            color = Color.GRAY
            isAntiAlias = true
            style = Paint.Style.FILL
            progressRect = RectF()
        }

        outlinePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 1f
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width == 0 || height == 0) {
            measure(0, 0)
            return
        }

        val radius = Math.min(width, height) / 2.toFloat() - 1f / 2.toFloat()
        canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, outlinePaint)


        val progressRadius = (width - 10) / 2.0f
        progressRect.left = width / 2.0f - progressRadius
        progressRect.right = width / 2.0f + progressRadius
        progressRect.top = height / 2.0f - progressRadius
        progressRect.bottom = height / 2.0f + progressRadius
        canvas.drawArc(progressRect, -90f, 360f * progress / 100, true, progressPaint)
    }


    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }
}