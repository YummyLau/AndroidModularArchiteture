package com.effective.android.base.toast

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {

    fun show(context: Context, text: String, duration: Int) {
        BaseToast.makeText(context, text, duration).show()
    }

    fun show(context: Context, text: String) {
        BaseToast.makeText(context, text, Toast.LENGTH_SHORT)
    }

    fun show(context: Context, @StringRes resId: Int) {
        BaseToast.makeText(context, resId, Toast.LENGTH_SHORT)
    }
}