package com.effective.android.base.util

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type


object GsonUtils {

    @JvmStatic
    fun getJsonString(obj: Any): String? {
        try {
            return Gson().toJson(obj)
        } catch (e: Exception) {
            Log.e("GsonUtils", e.message)
            return null
        }
    }

    fun <T> getJsonString(obj: T, typeToken: TypeToken<T>): String? {
        try {
            return Gson().toJson(obj, typeToken.type)
        } catch (e: Exception) {
            Log.e("GsonUtils", e.message)
            return null
        }

    }

    fun <T> getObj(jsonString: String, clazz: Class<T>): T? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        try {
            return Gson().fromJson(jsonString, clazz)
        } catch (t: Throwable) {
            Log.e("GsonUtils", "json - $jsonString", t)
        }

        return null
    }

    fun <T> getObj(jsonString: String, typeToken: TypeToken<T>): T? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        try {
            return Gson().fromJson(jsonString, typeToken.type)
        } catch (t: Throwable) {
            Log.e("GsonUtils", "json - $jsonString", t)
        }

        return null
    }

    fun <T> getParam(uri: Uri, key: String, type: Type): T? {
        val param = UriUtils.getStringQueryParameter(uri, key)
        if (TextUtils.isEmpty(param)) {
            return null
        }
        try {
            return Gson().fromJson(param, type)
        } catch (t: Throwable) {
            Log.e("GsonUtils", "$key-$param", t)
        }

        return null
    }

    fun <T> getExtra(extras: Bundle, key: String, type: Type): T? {
        val json = extras.getString(key)
        if (TextUtils.isEmpty(json)) {
            return null
        }
        try {
            return Gson().fromJson(json, type)
        } catch (t: Throwable) {
            Log.e("GsonUtils", "$key-$json", t)
        }

        return null
    }

    fun putExtra(intent: Intent, key: String, value: Any?) {
        if (value == null) {
            return
        }
        try {
            val json = Gson().toJson(value)
            intent.putExtra(key, json)
        } catch (t: Throwable) {
            Log.e("GsonUtils", key, t)
        }

    }

    fun <T> getValue(value: Any?, type: Type?, clazz: Class<T>): T? {
        if (value == null || type == null) {
            return null
        }
        var newValue: T? = null
        try {
            val gson = Gson()
            val json = gson.toJson(value)
            newValue = gson.fromJson(json, type)
        } catch (t: Throwable) {
            Log.e("GsonUtils", "value:$value, type:$type", t)
        }

        return newValue
    }

    fun getString(jsonObject: JsonObject, key: String): String? {
        var value: String? = null
        try {
            val jsonElement = if (jsonObject.has(key)) jsonObject.get(key) else null
            if (jsonElement != null && !jsonElement.isJsonNull) {
                value = if (jsonElement.isJsonObject) jsonElement.toString() else jsonObject.get(key).asString
            }
        } catch (e: Exception) {
            Log.e(GsonUtils::class.java.simpleName, "getString", e)
        }

        return value
    }

    fun getFloat(jsonObject: JsonObject, key: String, defaultValue: Float): Float {
        var value = defaultValue
        try {
            value = if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) jsonObject.get(key).asFloat else defaultValue
        } catch (e: Exception) {
            Log.e(GsonUtils::class.java.simpleName, "getLong", e)
        }

        return value
    }

    fun getLong(jsonObject: JsonObject, key: String, defaultValue: Long): Long {
        var value = defaultValue
        try {
            value = if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) jsonObject.get(key).asLong else defaultValue
        } catch (e: Exception) {
            Log.e(GsonUtils::class.java.simpleName, "getLong", e)
        }

        return value
    }

    fun getInt(jsonObject: JsonObject, key: String, defaultValue: Int): Int {
        var value = defaultValue
        try {
            value = if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) jsonObject.get(key).asInt else defaultValue
        } catch (e: Exception) {
            Log.e(GsonUtils::class.java.simpleName, "getInt", e)
        }

        return value
    }

    fun getBoolean(jsonObject: JsonObject, key: String, defaultValue: Boolean): Boolean {
        var value = defaultValue
        try {
            value = if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) jsonObject.get(key).asBoolean else defaultValue
        } catch (e: Exception) {
            Log.e(GsonUtils::class.java.simpleName, "getInt", e)
        }

        return value
    }

}
