package com.effective.android.permission

/**
 * 兼容android全版本权限请求结果
 * created by yummylau on 2019/09/04
 */
interface ResultCall {

    fun granted()

    fun denied(never: Boolean)
}
