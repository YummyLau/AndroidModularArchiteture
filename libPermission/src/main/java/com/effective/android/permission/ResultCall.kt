package com.effective.android.permission

interface ResultCall {

    fun granted()

    fun denied(never: Boolean)
}
