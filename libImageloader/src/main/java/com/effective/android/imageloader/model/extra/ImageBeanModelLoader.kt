package com.effective.android.imageloader.model.base64

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.effective.android.imageloader.model.ImageBean
import java.io.InputStream


/**
 * ImageBean 委托实现
 * Created by yummyLau on 2019/03/22.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

class ImageBeanModelLoader constructor(private val urlLoader: ModelLoader<GlideUrl, InputStream>) : BaseGlideUrlLoader<ImageBean>(urlLoader) {

    override fun handles(model: ImageBean): Boolean = true

    override fun buildLoadData(model: ImageBean, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? = urlLoader.buildLoadData(GlideUrl(model.url), width, height, options)

    /**
     * 可以用于处理一些非公开图片需要填写授权表单信息
     */
    override fun getHeaders(model: ImageBean?, width: Int, height: Int, options: Options?): Headers? {
//        return LazyHeaders.Builder()
//                .addHeader("Authorization") { getAuthToken() }
//                .build()
        return super.getHeaders(model, width, height, options)
    }

    override fun getUrl(model: ImageBean?, width: Int, height: Int, options: Options?): String {
        val imageWidth = model!!.width
        val imageHeight = model!!.height
        //这里可以处理宽高关系之后适配url，常见于nos图片服务器的链接适配
        return model!!.url
    }

}