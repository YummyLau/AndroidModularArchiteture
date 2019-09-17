package com.effective.android.example

import android.os.Bundle
import com.effective.android.base.activity.BaseActivity
import example.androidmodulararchiteture.R
class MainActivity : BaseActivity() {


    override fun getLayoutRes(): Int {
        return R.layout.app_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {

    }
}

