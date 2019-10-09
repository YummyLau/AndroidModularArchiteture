package com.debug

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.effective.android.base.activity.BaseActivity
import com.effective.android.base.systemui.StatusbarHelper
import kotlinx.android.synthetic.main.debug_activity_main_layout.*

open class DebugActivity : BaseActivity() {

    override fun getLayoutRes(): Int = R.layout.debug_activity_main_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.translucentStatusBar(this)
    }
}
