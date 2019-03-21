package com.effective.android.imageloader

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.gpu.*

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class ImageLoader : ImageLoaderApi {

    override fun clearRequest(imageView: ImageView) {

    }

    private fun loadTemp(imageView: ImageView, source: Any, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?, transformation: Transformation<Bitmap>) {
        var context = Utils.isValidContextForGlide(imageView)
        if (context != null) {
            val makeRequestOptions = RequestOptions.bitmapTransform(transformation)
            when (transformation) {
                is BrightnessFilterTransformation -> makeRequestOptions.dontAnimate()
                is SwirlFilterTransformation -> makeRequestOptions.dontAnimate()
                is KuwaharaFilterTransformation -> makeRequestOptions.dontAnimate()
                is VignetteFilterTransformation -> makeRequestOptions.dontAnimate()
            }
            Glide.with(imageView.context)
                    .asDrawable()
                    .load(source)
                    .apply(makeRequestOptions).apply {
                        if (requestOptions != null) {
                            apply(requestOptions)
                        }
                        if (requestListener != null) {
                            listener(requestListener)
                        }
                    }.into(imageView)
        }
    }

    @JvmOverloads
    fun circleGif(imageView: ImageView, source: Drawable) {
        var context = Utils.isValidContextForGlide(imageView)
        if (context != null) {
            val makeRequestOptions = RequestOptions.bitmapTransform(CircleCrop())
            Glide.with(imageView.context)
                    .asGif()
                    .load(source)
                    .apply(makeRequestOptions).into(imageView)
        }
    }

    @JvmOverloads
    override fun mask(imageView: ImageView, source: Drawable, mask: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, MultiTransformation<Bitmap>(CenterCrop(), MaskTransformation(mask)))
    }

    @JvmOverloads
    override fun mask(imageView: ImageView, source: String, mask: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, MultiTransformation<Bitmap>(CenterCrop(), MaskTransformation(mask)))
    }

    @JvmOverloads
    override fun corp(imageView: ImageView, source: String, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CropTransformation(corpWidth, corpHeight, corpType))
    }

    @JvmOverloads
    override fun corp(imageView: ImageView, source: Drawable, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CropTransformation(corpWidth, corpHeight, corpType))
    }

    @JvmOverloads
    override fun square(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CropSquareTransformation())
    }

    @JvmOverloads
    override fun square(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CropSquareTransformation())
    }

    @JvmOverloads
    override fun circle(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CircleCrop())
    }

    @JvmOverloads
    override fun circle(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, CircleCrop())
    }

    @JvmOverloads
    override fun colorFilter(imageView: ImageView, source: String, filterColor: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ColorFilterTransformation(filterColor))
    }

    @JvmOverloads
    override fun colorFilter(imageView: ImageView, source: Drawable, filterColor: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ColorFilterTransformation(filterColor))
    }

    @JvmOverloads
    override fun grayScale(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, GrayscaleTransformation())
    }

    @JvmOverloads
    override fun grayScale(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, GrayscaleTransformation())
    }

    @JvmOverloads
    override fun rounded(imageView: ImageView, source: String, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, RoundedCornersTransformation(radius, margin, cornerType))
    }

    @JvmOverloads
    override fun rounded(imageView: ImageView, source: Drawable, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, RoundedCornersTransformation(radius, margin, cornerType))
    }

    @JvmOverloads
    override fun blur(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, BlurTransformation(radius))
    }

    @JvmOverloads
    override fun blur(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, BlurTransformation(radius))
    }

    @JvmOverloads
    override fun rsBlur(imageView: ImageView, source: String, radius: Int, sampling: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SupportRSBlurTransformation(radius, sampling))
    }

    @JvmOverloads
    override fun rsBlur(imageView: ImageView, source: Drawable, radius: Int, sampling: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SupportRSBlurTransformation(radius, sampling))
    }

    override fun toon(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ToonFilterTransformation())
    }

    override fun toon(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ToonFilterTransformation())
    }

    override fun sepia(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SepiaFilterTransformation())
    }

    override fun sepia(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SepiaFilterTransformation())
    }

    override fun contrast(imageView: ImageView, source: String, contrast: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ContrastFilterTransformation(contrast))
    }

    override fun contrast(imageView: ImageView, source: Drawable, contrast: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, ContrastFilterTransformation(contrast))
    }

    override fun invert(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, InvertFilterTransformation())
    }

    override fun invert(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, InvertFilterTransformation())
    }

    override fun pixel(imageView: ImageView, source: String, pixel: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, PixelationFilterTransformation(pixel))
    }

    override fun pixel(imageView: ImageView, source: Drawable, pixel: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, PixelationFilterTransformation(pixel))
    }

    override fun sketch(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SketchFilterTransformation())
    }

    override fun sketch(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SketchFilterTransformation())
    }

    override fun swirl(imageView: ImageView, source: String, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SwirlFilterTransformation(radius, angle, pointF))
    }

    override fun swirl(imageView: ImageView, source: Drawable, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, SwirlFilterTransformation(radius, angle, pointF))
    }

    override fun brightness(imageView: ImageView, source: String, brightness: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, BrightnessFilterTransformation(brightness))
    }

    override fun brightness(imageView: ImageView, source: Drawable, brightness: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, BrightnessFilterTransformation(brightness))
    }

    override fun kuawahara(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, KuwaharaFilterTransformation(radius))
    }

    override fun kuawahara(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, KuwaharaFilterTransformation(radius))
    }

    override fun vignette(imageView: ImageView, source: String, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, VignetteFilterTransformation(center, color, start, end))
    }

    override fun vignette(imageView: ImageView, source: Drawable, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener, VignetteFilterTransformation(center, color, start, end))
    }
}
