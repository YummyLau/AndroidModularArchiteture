package com.effective.android.permission

interface MutilResultCall {

    fun granted(permission: String)

    fun denied(permission: String, never: Boolean)
}
