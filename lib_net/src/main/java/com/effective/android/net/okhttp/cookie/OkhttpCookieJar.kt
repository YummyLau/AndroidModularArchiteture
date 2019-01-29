package com.effective.android.net.okhttp.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class OkHttpCookieJar(context: Context) : CookieJar {

    private var cookieStore: PersistentCookieStore? = null

    init {
        cookieStore = PersistentCookieStore(context)
    }


    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        if (cookies != null && !cookies.isEmpty()) {
            for (item in cookies) {
                cookieStore!!.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore!!.get(url)
    }
}