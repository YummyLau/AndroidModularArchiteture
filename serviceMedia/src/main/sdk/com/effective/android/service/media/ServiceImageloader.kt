package com.effective.android.service.media

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

interface ServiceImageloader {

    fun load(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

}