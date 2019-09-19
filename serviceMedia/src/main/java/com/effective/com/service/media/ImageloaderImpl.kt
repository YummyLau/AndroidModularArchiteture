package com.effective.com.service.media

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.effective.android.imageloader.ImageLoader
import com.effective.android.service.media.ServiceImageloader
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ServiceImageloader::class])
class ImageloaderImpl : ServiceImageloader {

    private val imageloader: ImageLoader = ImageLoader()

    @JvmOverloads
    override fun load(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.load(imageView, source, requestOptions, requestListener)
    }
}