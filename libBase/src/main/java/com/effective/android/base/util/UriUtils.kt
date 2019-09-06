package com.effective.android.base.util

import android.net.Uri


object UriUtils {

    @JvmStatic
    fun getIntQueryParameter(uri: Uri, key: String, defaultValue: Int): Int {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return Integer.parseInt(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getLongQueryParameter(uri: Uri, key: String, defaultValue: Long): Long {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return java.lang.Long.parseLong(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getBooleanQueryParameter(uri: Uri, key: String, defaultValue: Boolean): Boolean {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return java.lang.Boolean.parseBoolean(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getShortQueryParameter(uri: Uri, key: String, defaultValue: Short): Short {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return java.lang.Short.parseShort(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getFloatQueryParameter(uri: Uri, key: String, defaultValue: Float): Float {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return java.lang.Float.parseFloat(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getDoubleQueryParameter(uri: Uri, key: String, defaultValue: Double): Double {
        try {
            val value = uri.getQueryParameter(key) ?: return defaultValue
            return java.lang.Double.parseDouble(value)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    @JvmStatic
    fun getStringQueryParameter(uri: Uri, key: String): String? {
        try {
            return uri.getQueryParameter(key)
        } catch (e: Exception) {
            return null
        }
    }

}
