package com.effective.android.net.okhttp

import android.util.Log
import com.effective.android.net.MediaTypes

import com.effective.android.net.okhttp.api.HttpApi

import java.io.File
import java.io.IOException

import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody

/**
 * 封装常规请求
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
//class HttpManager : HttpApi {
//
//    override fun post(url: String, mapParams: Map<String, String>): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    operator fun get(url: String): String? {
//        var result: String? = null
//        try {
//            val request = Request.Builder()
//                    .url(url)
//                    .build()
//            val response = HttpClient.instance!!.newCall(request).execute()
//            result = response.body()!!.string()
//        } catch (e: IOException) {
//            Log.e(TAG, e.message)
//        }
//
//        return result
//    }
//
//
//    operator fun post(url: String, json: String): String? {
//        var result: String? = null
//        try {
//            val body = RequestBody.create(MediaTypes.JSON, json)
//            val request = Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build()
//            val response = HttpClient.instance!!.newCall(request).execute()
//            result = response.body()!!.string()
//        } catch (e: IOException) {
//            Log.e(TAG, e.message)
//        }
//
//        return result
//    }
//
//    fun post(url: String, mapParams: Map<String, String>?): String? {
//        var result: String? = null
//        try {
//            val builder = FormBody.Builder()
//            if (mapParams != null && !mapParams.isEmpty()) {
//                for (key in mapParams.keys) {
//                    builder.add(key, mapParams[key])
//                }
//            }
//            val request = Request.Builder()
//                    .url(url)
//                    .post(builder.build())
//                    .build()
//            val response = HttpClient.instance!!.newCall(request).execute()
//            result = response.body()!!.string()
//        } catch (e: IOException) {
//            Log.e(TAG, e.message)
//        }
//
//        return result
//    }
//
//    fun uploadFile(url: String, pathName: String, fileName: String): String? {
//        var result: String? = null
//        try {
//            val body = RequestBody.create(MediaTypes.FILE, File(pathName))
//            val builder = MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart(MediaTypes.FILE.type(), fileName, body)
//            val request = Request.Builder()
//                    .url(url)
//                    .post(builder.build())
//                    .build()
//            val response = HttpClient.instance!!.newCall(request).execute()
//            result = response.body()!!.string()
//        } catch (e: IOException) {
//            Log.e(TAG, e.message)
//        }
//
//        return result
//    }
//
//    companion object {
//
//        private val TAG = HttpManager::class.java.simpleName
//    }
//}
