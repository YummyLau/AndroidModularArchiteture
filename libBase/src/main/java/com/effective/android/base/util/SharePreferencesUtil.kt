package com.effective.android.base.util
import android.content.Context
import android.content.SharedPreferences

object SharePreferencesUtil {

    private const val NAME = "data"

    @JvmStatic
    fun getInstance(context: Context, name: String = NAME, mode: Int = Context.MODE_PRIVATE): SharedPreferences = context.getSharedPreferences(name, mode)

    @JvmStatic
    fun putBoolean(context: Context, key: String, value: Boolean?, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        val editor = sharedPreference.edit()
        editor.putBoolean(key, value!!)
        return editor.commit()
    }

    @JvmStatic
    fun putInt(context: Context, key: String, value: Int, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        val editor = sharedPreference.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    @JvmStatic
    fun putFloat(context: Context, key: String, value: Float, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        val editor = sharedPreference.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    @JvmStatic
    fun putLong(context: Context, key: String, value: Long, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        val editor = sharedPreference.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    @JvmStatic
    fun putString(context: Context, key: String, value: String, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    @JvmStatic
    fun getString(context: Context, key: String, defValue: String, name: String = NAME, mode: Int = Context.MODE_PRIVATE): String? {
        val sharedPreference = getInstance(context, name, mode)
        return sharedPreference.getString(key, defValue)
    }

    @JvmStatic
    fun getInt(context: Context, key: String, defValue: Int, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Int {
        val sharedPreference = getInstance(context, name, mode)
        return sharedPreference.getInt(key, defValue)
    }

    @JvmStatic
    fun getFloat(context: Context, key: String, defValue: Float?, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Float {
        val sharedPreference = getInstance(context, name, mode)
        return sharedPreference.getFloat(key, defValue!!)
    }

    @JvmStatic
    fun getBoolean(context: Context, key: String,
                   defValue: Boolean?, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Boolean {
        val sharedPreference = getInstance(context, name, mode)
        return sharedPreference.getBoolean(key, defValue!!)
    }

    @JvmStatic
    fun getLong(context: Context, key: String, defValue: Long, name: String = NAME, mode: Int = Context.MODE_PRIVATE): Long {
        val sharedPreference = getInstance(context, name, mode)
        return sharedPreference.getLong(key, defValue)
    }
}
