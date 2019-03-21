package com.effective.android.imageloader


import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory

import java.nio.ByteBuffer

/**
 * base64 loaderFactory
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class Base64ModelLoaderFactory : ModelLoaderFactory<String, ByteBuffer> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, ByteBuffer> {
        return Base64ModelLoader()
    }

    override fun teardown() {

    }
}
