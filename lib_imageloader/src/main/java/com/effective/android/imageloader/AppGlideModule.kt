package com.effective.android.imageloader

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator

/**
 * 自定义module
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 *
 *
 * registerComponents 方法，可注册所属于所需要处理的组件逻辑，比如加载特定类型的资源
 */
@GlideModule
class AppGlideModule : com.bumptech.glide.module.AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //默认使用支持base64实现
        //        registry.prepend(String.class, ByteBuffer.class, new Base64ModelLoaderFactory());
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        //内存缓存控制
        val memorySizeCalculator = MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2f)
                .build()
        builder.setMemoryCache(LruResourceCache(memorySizeCalculator.memoryCacheSize.toLong()))

        //Bitmap 池
        val calculator = MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3f)
                .build()
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))

        //磁盘缓存
        builder.setDiskCache(
                ExternalPreferredCacheDiskCacheFactory(context, (250 * 1024 * 1024).toLong()))

        //        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        super.applyOptions(context, builder)
    }
}
