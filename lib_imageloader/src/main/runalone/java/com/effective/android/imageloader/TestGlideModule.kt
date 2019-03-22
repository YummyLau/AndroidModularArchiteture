package com.effective.android.imageloader

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.effective.android.imageloader.module.ConfigGlideModule
import com.effective.android.imageloader.progress.DispatchingProgressManager
import com.effective.android.imageloader.progress.OkHttpProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.InputStream


@Excludes(ConfigGlideModule::class)
@GlideModule
class TestGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        val response = chain.proceed(request)
                        val listener = DispatchingProgressManager()
                        return response.newBuilder()
                                .body(OkHttpProgressResponseBody(request.url(), response.body()!!, listener))
                                .build()
                    }
                })
                .build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }

    /**
     * 实现自定义glide配置
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {

        val memorySizeCalculator = MemorySizeCalculator.Builder(context)
                .build()

        //内存缓存控制
        builder.setMemoryCache(LruResourceCache(memorySizeCalculator.memoryCacheSize.toLong()))

        //Bitmap 池
        builder.setBitmapPool(LruBitmapPool(memorySizeCalculator.bitmapPoolSize.toLong()))

        val diskCacheSize = 250 * 1024 * 1024L

        //外部磁盘缓存
        builder.setDiskCache(
                ExternalPreferredCacheDiskCacheFactory(context, diskCacheSize))

        //私有目录缓存
//        builder.setDiskCache(
//                InternalCacheDiskCacheFactory(context, diskCacheSize))

        //默认像素设置
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
        super.applyOptions(context, builder)
    }


    /**
     * 不解析 AndroidManifest 配置的GlideModule
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
