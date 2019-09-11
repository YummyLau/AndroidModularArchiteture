package com.effective.android.component.weibo

interface WeiboSdk {

    fun getMainPath(): String

    fun gotoMainActivity()

    fun getWeiboMainFragment() : androidx.fragment.app.Fragment
}
