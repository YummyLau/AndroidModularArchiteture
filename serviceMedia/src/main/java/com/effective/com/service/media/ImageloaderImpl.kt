package com.effective.com.service.media

import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.effective.android.imageloader.ImageLoader
import com.effective.android.service.media.ServiceImageloader
import com.plugin.component.anno.AutoInjectImpl
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

@AutoInjectImpl(sdk = [ServiceImageloader::class])
class ImageloaderImpl : ServiceImageloader {

    private val imageloader: ImageLoader = ImageLoader()

    @JvmOverloads
    override fun load(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.load(imageView, source, requestOptions, requestListener)
    }

    override fun clearRequest(imageView: ImageView) {
        imageloader.clearRequest(imageView)
    }

    override fun clearDiskCache(context: Context) {
        imageloader.clearDiskCache(context)
    }

    override fun clearMemory(context: Context) {
        imageloader.clearMemory(context)
    }

    override fun load(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.load(imageView, source, requestOptions, requestListener)
    }

    override fun mask(imageView: ImageView, source: String, mask: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.mask(imageView, source, mask, requestOptions, requestListener)
    }

    override fun mask(imageView: ImageView, source: Drawable, mask: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.mask(imageView, source, mask, requestOptions, requestListener)
    }

    override fun corp(imageView: ImageView, source: String, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.corp(imageView, source, corpWidth, corpHeight, corpType, requestOptions, requestListener)
    }

    override fun corp(imageView: ImageView, source: Drawable, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.corp(imageView, source, corpWidth, corpHeight, corpType, requestOptions, requestListener)
    }

    override fun square(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.square(imageView, source, requestOptions, requestListener)
    }

    override fun square(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.square(imageView, source, requestOptions, requestListener)
    }

    override fun circle(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.circle(imageView, source, requestOptions, requestListener)
    }

    override fun circle(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.circle(imageView, source, requestOptions, requestListener)
    }

    override fun colorFilter(imageView: ImageView, source: String, filterColor: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.colorFilter(imageView, source, filterColor, requestOptions, requestListener)
    }

    override fun colorFilter(imageView: ImageView, source: Drawable, filterColor: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.colorFilter(imageView, source, filterColor, requestOptions, requestListener)
    }

    override fun grayScale(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.grayScale(imageView, source, requestOptions, requestListener)
    }

    override fun grayScale(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.grayScale(imageView, source, requestOptions, requestListener)
    }

    override fun rounded(imageView: ImageView, source: String, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.rounded(imageView, source, radius, margin, cornerType, requestOptions, requestListener)
    }

    override fun rounded(imageView: ImageView, source: Drawable, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.rounded(imageView, source, radius, margin, cornerType, requestOptions, requestListener)
    }

    override fun blur(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.blur(imageView, source, radius, requestOptions, requestListener)
    }

    override fun blur(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.blur(imageView, source, radius, requestOptions, requestListener)
    }

    override fun rsBlur(imageView: ImageView, source: String, radius: Int, sampling: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.rsBlur(imageView, source, radius, sampling, requestOptions, requestListener)
    }

    override fun rsBlur(imageView: ImageView, source: Drawable, radius: Int, sampling: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.rsBlur(imageView, source, radius, sampling, requestOptions, requestListener)
    }

    override fun toon(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.toon(imageView, source, requestOptions, requestListener)
    }

    override fun toon(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.toon(imageView, source, requestOptions, requestListener)
    }

    override fun sepia(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.sepia(imageView, source, requestOptions, requestListener)
    }

    override fun sepia(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.sepia(imageView, source, requestOptions, requestListener)
    }

    override fun contrast(imageView: ImageView, source: String, contrast: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.contrast(imageView, source, contrast, requestOptions, requestListener)
    }

    override fun contrast(imageView: ImageView, source: Drawable, contrast: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.contrast(imageView, source, contrast, requestOptions, requestListener)
    }

    override fun invert(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.invert(imageView, source, requestOptions, requestListener)
    }

    override fun invert(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.invert(imageView, source, requestOptions, requestListener)
    }

    override fun pixel(imageView: ImageView, source: String, pixel: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.pixel(imageView, source, pixel, requestOptions, requestListener)
    }

    override fun pixel(imageView: ImageView, source: Drawable, pixel: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.pixel(imageView, source, pixel, requestOptions, requestListener)
    }

    override fun sketch(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.sketch(imageView, source, requestOptions, requestListener)
    }

    override fun sketch(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.sketch(imageView, source, requestOptions, requestListener)
    }

    override fun swirl(imageView: ImageView, source: String, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.swirl(imageView, source, radius, angle, pointF, requestOptions, requestListener)
    }

    override fun swirl(imageView: ImageView, source: Drawable, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.swirl(imageView, source, radius, angle, pointF, requestOptions, requestListener)
    }

    override fun brightness(imageView: ImageView, source: String, brightness: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.brightness(imageView, source, brightness, requestOptions, requestListener)
    }

    override fun brightness(imageView: ImageView, source: Drawable, brightness: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.brightness(imageView, source, brightness, requestOptions, requestListener)
    }

    override fun kuawahara(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.kuawahara(imageView, source, radius, requestOptions, requestListener)
    }

    override fun kuawahara(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.kuawahara(imageView, source, radius, requestOptions, requestListener)
    }

    override fun vignette(imageView: ImageView, source: String, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.vignette(imageView, source, center, color, start, end, requestOptions, requestListener)
    }

    override fun vignette(imageView: ImageView, source: Drawable, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        imageloader.vignette(imageView, source, center, color, start, end, requestOptions, requestListener)
    }
}