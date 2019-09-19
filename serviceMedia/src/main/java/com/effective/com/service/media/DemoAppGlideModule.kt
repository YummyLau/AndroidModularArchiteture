package com.effective.com.service.media

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.request.RequestOptions


/**
 * app 全局module，也集合了所有 LibraryGlideModules 的功能
 * @GlideModule 用于收集所有 DemoAppGlideModule 和 LibraryGlideModule
 *
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 *
 * registerComponents 方法，可注册所属于所需要处理的组件逻辑，比如加载特定类型的资源
 */
@Excludes(ConfigGlideModule::class)
@GlideModule
class ImageAppGlideModule : com.bumptech.glide.module.AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
       // Glide 默认使用支持base64实现，以下指示演示
    }

    /**
     * 实现自定义glide配置
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {

        val memorySizeCalculator = MemorySizeCalculator.Builder(context)
                .build()

       // 内存缓存控制
        builder.setMemoryCache(LruResourceCache(memorySizeCalculator.memoryCacheSize.toLong()))

       // Bitmap 池
        builder.setBitmapPool(LruBitmapPool(memorySizeCalculator.bitmapPoolSize.toLong()))

        val diskCacheSize = 250 * 1024 * 1024L

       // 外部磁盘缓存
        builder.setDiskCache(
                ExternalPreferredCacheDiskCacheFactory(context, diskCacheSize))

       // 私有目录缓存
////        builder.setDiskCache(
////                InternalCacheDiskCacheFactory(context, diskCacheSize))

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
