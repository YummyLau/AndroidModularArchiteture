package com.effective.android.net.retrofit.covert

import android.util.Log

import com.effective.android.net.MediaTypes

import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * 请求数据转字节
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

class ToByteConvertFactory private constructor() : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        Log.e(TAG, "convert: Converter<?, RequestBody>000" + type + "    " + ByteArray::class.java)
        return if ("byte[]" == type!!.toString() + "") {
            Converter<ResponseBody, ByteArray> { value ->
                Log.e(TAG, "convert: Converter<ResponseBody, ?>")
                value.bytes()
            }
        } else super.responseBodyConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        Log.e(TAG, "convert: Converter<?, RequestBody>000")
        return if ("byte[]" == type!!.toString() + "") {
            Converter<ByteArray, RequestBody> { value ->
                Log.e(TAG, "convert: Converter<?, RequestBody>")
                RequestBody.create(MediaTypes.STREAM, value)
            }
        } else super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    companion object {

        private val TAG = ToByteConvertFactory::class.java.simpleName

        fun create(): ToByteConvertFactory {
            return ToByteConvertFactory()
        }
    }
}
