package com.effective.android.imageloader

import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.widget.ImageView

import androidx.annotation.DrawableRes
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
interface ImageLoaderApi {

    fun clearRequest(imageView: ImageView)

    //遮罩
    fun mask(imageView: ImageView, source: String, @DrawableRes mask: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun mask(imageView: ImageView, source: Drawable, @DrawableRes mask: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //裁剪,上中下
    fun corp(imageView: ImageView, source: String, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun corp(imageView: ImageView, source: Drawable, corpWidth: Int, corpHeight: Int, corpType: CropTransformation.CropType, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //裁剪，正方形
    fun square(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun square(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //裁剪，圆形
    fun circle(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun circle(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //颜色过滤
    fun colorFilter(imageView: ImageView, source: String, filterColor: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun colorFilter(imageView: ImageView, source: Drawable, filterColor: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //灰階
    fun grayScale(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun grayScale(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //圆角
    fun rounded(imageView: ImageView, source: String, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun rounded(imageView: ImageView, source: Drawable, radius: Int, margin: Int, cornerType: RoundedCornersTransformation.CornerType, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //高斯模糊
    fun blur(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun blur(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //毛玻璃
    fun rsBlur(imageView: ImageView, source: String, radius: Int, sampling: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun rsBlur(imageView: ImageView, source: Drawable, radius: Int, sampling: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //卡通
    fun toon(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun toon(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //深褐色
    fun sepia(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun sepia(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //对比度
    fun contrast(imageView: ImageView, source: String, contrast: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun contrast(imageView: ImageView, source: Drawable, contrast: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //反色
    fun invert(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun invert(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //像素
    fun pixel(imageView: ImageView, source: String, pixel: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun pixel(imageView: ImageView, source: Drawable, pixel: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //素描
    fun sketch(imageView: ImageView, source: String, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun sketch(imageView: ImageView, source: Drawable, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //涡旋
    fun swirl(imageView: ImageView, source: String, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun swirl(imageView: ImageView, source: Drawable, radius: Float, angle: Float, pointF: PointF, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //亮度
    fun brightness(imageView: ImageView, source: String, brightness: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun brightness(imageView: ImageView, source: Drawable, brightness: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //水彩
    fun kuawahara(imageView: ImageView, source: String, radius: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun kuawahara(imageView: ImageView, source: Drawable, radius: Int, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    //暈影
    fun vignette(imageView: ImageView, source: String, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

    fun vignette(imageView: ImageView, source: Drawable, center: PointF, color: FloatArray, start: Float, end: Float, requestOptions: RequestOptions? = null, requestListener: RequestListener<Drawable>? = null)

}
