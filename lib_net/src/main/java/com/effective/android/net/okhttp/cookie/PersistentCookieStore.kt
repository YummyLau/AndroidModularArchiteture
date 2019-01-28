package com.effective.android.net.okhttp.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

import okhttp3.Cookie
import okhttp3.HttpUrl
import kotlin.experimental.and


/**
 * A persistent cookie store which implements the Apache HttpClient CookieStore interface.
 * Cookies are stored and will persist on the user's device between application sessions since they
 * are serialized and stored in SharedPreferences. Instances of this class are
 * designed to be used with AsyncHttpClient#setCookieStore, but can also be used with a
 * regular old apache HttpClient/HttpContext if you prefer.
 *
 *
 * 本地存储cookie
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class PersistentCookieStore(context: Context) {

    /**
     * 以访问百度为例子，在Cookies中有
     * key ： http://e.baidu.com
     * value ： 可能存在多个Cookie
     * [
     * {
     * "Name" : "BAIDUID",
     * "Vaule" : "D9CCD88E8E439DAE5623C38A9F131128",
     * "Domain" : ".baidu.com",
     * "Path" " : "/",
     * "Expires/Max-Age" : "2019-07-19T09:40:47.337z",
     * "Size" : "44",
     * "Http" : "",
     * "Secure" : "",
     * "Same" " ""
     * },
     * {
     * ...
     * }
     * ]
     */
    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>>
    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0)
        cookies = HashMap()

        //将持久化的cookies缓存到内存中 即map cookies
        val prefsMap = cookiePrefs.all
        for ((key, obj) in prefsMap) {

            //寻找 host_ 开头的项
            if (!TextUtils.isEmpty(key) && key.startsWith(HOST_PRE) && obj is String) {
                //索取所有cookies id
                val cookieId = TextUtils.split(obj, ",")
                for (name in cookieId) {
                    val encodedCookie = cookiePrefs.getString(name, null)
                    if (encodedCookie != null) {
                        val decodedCookie = decodeCookie(encodedCookie)
                        if (decodedCookie != null) {
                            if (!cookies.containsKey(key)) {
                                cookies[key] = ConcurrentHashMap()
                            }
                            cookies[key]!![name] = decodedCookie
                        }
                    }
                }
            }
        }
    }

    /**
     * example
     * host_www.google.com
     * @param url
     * @return
     */
    private fun getUrlHostID(url: HttpUrl): String {
        return HOST_PRE + url.host()
    }

    /**
     * {
     * "Name" : "BAIDUID",
     * "Vaule" : "D9CCD88E8E439DAE5623C38A9F131128",
     * "Domain" : ".baidu.com",
     * "Path" " : "/",
     * "Expires/Max-Age" : "2019-07-19T09:40:47.337z",
     * "Size" : "44",
     * "Http" : "",
     * "Secure" : "",
     * "Same" " ""
     * }
     *
     * @param cookie
     * @return BAIDUID@.baidu.com
     */
    protected fun getCookieId(cookie: Cookie): String {
        return cookie.name() + "@" + cookie.domain()
    }

    /**
     * 一个url可能对应多个 cookie信息
     *
     * @param url    请求链接
     * @param cookie 对应其中的cookie
     */
    fun add(url: HttpUrl, cookie: Cookie) {
        val name = getCookieId(cookie)

        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (cookie.persistent()) {
            if (!cookies.containsKey(getUrlHostID(url))) {
                cookies[HOST_PRE + getUrlHostID(url)] = ConcurrentHashMap()
            }
            cookies[getUrlHostID(url)]!![name] = cookie
        } else {
            if (cookies.containsKey(getUrlHostID(url))) {
                cookies[getUrlHostID(url)]?.remove(name)
            } else {
                return
            }
        }

        //更新本地缓存
        val prefsWriter = cookiePrefs.edit()
        // 如 请求www.baidu.com，www.baidu.com 为 url.host()
        // 存在多个cookie，映射到 BAIDUID@.baidu.com ，BAIDUID@.baidu.com ， BAIDUID@.baidu.com ,...多个cookieToken
        prefsWriter.putString(getUrlHostID(url), TextUtils.join(",", cookies[getUrlHostID(url)]!!.keys))
        //在单独存储对应的cookie
        prefsWriter.putString(name, encodeCookie(SerializableOkHttpCookie(cookie)))
        prefsWriter.apply()
    }


    operator fun get(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        if (cookies.containsKey(getUrlHostID(url))) {
            val cookies = this.cookies[getUrlHostID(url)]!!.values
            for (cookie in cookies) {
                if (isCookieExpired(cookie)) {
                    remove(url, cookie)
                } else {
                    ret.add(cookie)
                }
            }
        }
        return ret
    }

    fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
        return true
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieId(cookie)

        if (cookies.containsKey(getUrlHostID(url)) && cookies[getUrlHostID(url)]!!.containsKey(name)) {
            cookies[getUrlHostID(url)]!!.remove(name)

            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name)
            }

            //更新url对应的cookies
            prefsWriter.putString(getUrlHostID(url), TextUtils.join(",", cookies[getUrlHostID(url)]!!.keys))
            prefsWriter.apply()
            return true
        } else {
            return false
        }
    }

    fun getCookies(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys)
            ret.addAll(cookies[key]!!.values)

        return ret
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected fun encodeCookie(cookie: SerializableOkHttpCookie?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(TAG, "IOException in encodeCookie", e)
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableOkHttpCookie).getCookie()
        } catch (e: IOException) {
            Log.d(TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e)
        }

        return cookie
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element and 0xFF
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    companion object {

        private val TAG = PersistentCookieStore::class.java.simpleName
        private val COOKIE_PREFS = "CookiePrefsFile"
        private val HOST_PRE = "host_"

        private fun isCookieExpired(cookie: Cookie): Boolean {
            return cookie.expiresAt() < System.currentTimeMillis()
        }
    }
}
