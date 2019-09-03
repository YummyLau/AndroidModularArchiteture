package com.effective.android.net.retrofit.type


import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Type {
    companion object {
        val GSON = 0
        val BYTE = 1
        val STRING = 2
    }
}
