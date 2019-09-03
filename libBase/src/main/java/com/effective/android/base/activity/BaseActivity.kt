package com.effective.android.base.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    fun contentView(): View = window.decorView.findViewById<View>(android.R.id.content) as View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }
}