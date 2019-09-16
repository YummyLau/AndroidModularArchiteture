package com.effective.android.base.toast

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {

    @JvmStatic
    fun show(context: Context, text: String, duration: Int) {
        BaseToast.makeText(context, text, duration).show()
    }

    @JvmStatic
    fun show(context: Context, text: String) {
        show(context, text, Toast.LENGTH_SHORT)
    }

    @JvmStatic
    fun show(context: Context, @StringRes resId: Int) {
        show(context, context.getString(resId), Toast.LENGTH_SHORT)
    }
}