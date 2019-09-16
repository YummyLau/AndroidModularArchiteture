package com.effective.android.base.toast

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes

import com.effective.android.base.R

import java.lang.reflect.Field


/**
 * 全局toast样式钩子
 * Created by yummylau on 2018/02/13.
 */

class BaseToast private constructor(private val mContext: Context, text: CharSequence, duration: Int) {

    private val mToast: Toast?
    private val textView: TextView

    init {
        val v = LayoutInflater.from(mContext).inflate(R.layout.base_toast_global_layout, null)
        textView = v.findViewById(R.id.tvToastContent)
        textView.text = text
        mToast = Toast(mContext)
        mToast.duration = duration
        mToast.view = v
    }

    fun setText(@StringRes resId: Int) {
        textView.text = mContext.getText(resId)
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setDuration(duration: Int) {
        mToast!!.duration = duration
    }

    fun show() {
        mToast?.show()
    }

    fun cancel() {
        mToast?.cancel()
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mToast?.setGravity(gravity, xOffset, yOffset)
    }

    companion object {

        fun makeText(context: Context, @StringRes resId: Int, duration: Int): BaseToast {
            return makeText(context, context.getText(resId), duration)
        }

        fun makeText(context: Context, text: CharSequence, duration: Int): BaseToast {
            return BaseToast(context, text, duration)
        }

        private fun setContext(view: View, context: Context) {
            try {
                val field = View::class.java.getDeclaredField("mContext")
                field.isAccessible = true
                field.set(view, context)
            } catch (throwable: Throwable) {
                Log.e(BaseToast::class.java.simpleName, "setContext", throwable)
            }

        }
    }
}
