package com.effective.android.service.net

interface ServiceNet {
    fun <T> service(baseUrl: String, type: Type, tClass: Class<T>): T

    fun <T> serviceWithoutRx(baseUrl: String, type: Type, tClass: Class<T>): T
}