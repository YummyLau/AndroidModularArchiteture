package com.effective.android.lib_imageloader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;

/**
 * 自定义module
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 * <p>
 * registerComponents 方法，可注册所属于所需要处理的组件逻辑，比如加载特定类型的资源
 */
@GlideModule
public class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //默认使用支持base64实现
//        registry.prepend(String.class, ByteBuffer.class, new Base64ModelLoaderFactory());
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

        //内存缓存控制
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(memorySizeCalculator.getMemoryCacheSize()));

        //Bitmap 池
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));

        //磁盘缓存
        builder.setDiskCache(
                new ExternalPreferredCacheDiskCacheFactory(context, 250 * 1024 * 1024));

        //        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        super.applyOptions(context, builder);
    }
}
