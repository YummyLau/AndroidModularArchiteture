package com.effective.android.net.okhttp.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class OkhttpCookieJar(context: Context) : CookieJar{


    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        if (cookies != null && !cookies.isEmpty()) {
            for (item in cookies) {
                cookieStore.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieStore.get(url)
    }
}