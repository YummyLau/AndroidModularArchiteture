package com.effective.android.imageloader.model.base64


import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.effective.android.imageloader.model.ImageBean
import java.io.InputStream


/**
 * ImageBean 委托实现
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class ImageBeanModelLoaderFactory : ModelLoaderFactory<ImageBean, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageBean, InputStream> = ImageBeanModelLoader(multiFactory.build(GlideUrl::class.java, InputStream::class.java))

    override fun teardown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
