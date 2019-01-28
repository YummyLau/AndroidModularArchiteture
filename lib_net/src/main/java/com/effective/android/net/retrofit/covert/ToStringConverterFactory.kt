package com.effective.android.net.retrofit.covert


import com.effective.android.net.MediaTypes
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException

/**
 * string 转化
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

class ToStringConverterFactory private constructor() : Converter.Factory() {


    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if (String::class.java == type) {
            return object : Converter<ResponseBody, String> {
                override fun convert(value: ResponseBody): String {
                    return value.toString()
                }
            }
        }
        return null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (String::class.java == type) {
            Converter<String, RequestBody> { value -> RequestBody.create(MediaTypes.TEXT, value) }
        } else null
    }

    companion object {
        fun create(): ToStringConverterFactory {
            return ToStringConverterFactory()
        }
    }
}

