package com.effective.android.base.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator


object AnimationUtils {

    val LINEAR_INTERPOLATOR: Interpolator = LinearInterpolator()
    val DECELERATE_INTERPOLATOR: Interpolator = DecelerateInterpolator()
    val ACCELERATE_INTERPOLATOR: Interpolator = AccelerateInterpolator()
    val ACCELERATE_DECELERATE_INTERPOLATOR: Interpolator = AccelerateDecelerateInterpolator()
    val ANTICIPATE_OVERSHOOT_INTERPOLATOR: Interpolator = AnticipateOvershootInterpolator()
    /**
     * 缩放且Y轴位移的组合属性动画
     */
    @JvmStatic
    fun createAnimator(target: View, scaleFrom: Float, scaleTo: Float,
                       translationYFrom: Float, translationYTo: Float,
                       duration: Long): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", scaleFrom, scaleTo)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", scaleFrom, scaleTo)
        val translationY = PropertyValuesHolder.ofFloat("translationY", translationYFrom, translationYTo)
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, scaleX, scaleY, translationY)
        objectAnimator.duration = duration
        objectAnimator.interpolator = DECELERATE_INTERPOLATOR
        return objectAnimator
    }
}
