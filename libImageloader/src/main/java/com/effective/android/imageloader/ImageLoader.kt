package com.effective.android.imageloader

import android.content.Context
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
 * api提供：
 * 1. 清除磁盘缓存
 * 2. 清除内存缓存
 * 3. 各类图片显示
 * 4. 取消图片请求
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class ImageLoader : ImageLoaderApi {

    override fun clearRequest(imageView: ImageView) {
        Glide.with(imageView.context).clear(imageView)
    }

    override fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    override fun clearMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    private fun loadTemp(imageView: ImageView, source: Any, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null, transformation: Transformation<Bitmap>? = null) {
        var context = Utils.isValidContextForGlide(imageView)
        if (context != null) {
            var requestOption: RequestOptions? = requestOptions
            if (requestOption == null) {
                requestOption = RequestOptions()
                if(transformation != null){
                    requestOption = requestOption.transform(transformation)
                    when (transformation) {
                        is BrightnessFilterTransformation -> requestOption.dontAnimate()
                        is SwirlFilterTransformation -> requestOption.dontAnimate()
                        is KuwaharaFilterTransformation -> requestOption.dontAnimate()
                        is VignetteFilterTransformation -> requestOption.dontAnimate()
                    }
                }
            }
            val requestBuilder = Glide.with(imageView.context)
                    .asDrawable()
                    .load(source)
                    .apply(requestOption)
                    .listener(requestListener)
            requestBuilder.into(imageView)
        }
    }

    @JvmOverloads
    override fun load(imageView: ImageView, source: String, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener)
    }

    @JvmOverloads
    override fun load(imageView: ImageView, source: Drawable, requestOptions: RequestOptions?, requestListener: RequestListener<Drawable>?) {
        loadTemp(imageView, source, requestOptions, requestListener)
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
