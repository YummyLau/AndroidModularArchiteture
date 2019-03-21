package com.effective.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.RequestOptions.overrideOf
import com.bumptech.glide.request.target.Target
import jp.wasabeef.example.glide.px
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.CropTransformation.CropType
import jp.wasabeef.glide.transformations.gpu.*

/**
 * Created by Wasabeef on 2015/01/11.
 */
class MainAdapter(
        private val context: Context,
        private val dataSet: MutableList<Type>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    enum class Type {
        Mask,
        NinePatchMask,
        CropTop,
        CropCenter,
        CropBottom,
        CropSquare,
        CropCircle,
        ColorFilter,
        Grayscale,
        RoundedCorners,
        Blur,
        SupportRSBlur,
        Toon,
        Sepia,
        Contrast,
        Invert,
        Pixel,
        Sketch,
        Swirl,
        Brightness,
        Kuawahara,
        Vignette
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (dataSet[position]) {
            MainAdapter.Type.Mask -> {
                ImageLoader().mask(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, R.drawable.mask_starfish)
            }

            MainAdapter.Type.NinePatchMask ->
                ImageLoader().mask(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, R.drawable.mask_chat_right)

            Type.CropTop ->
                ImageLoader().corp(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 300.px, 100.px, CropType.TOP)

            Type.CropCenter ->
                ImageLoader().corp(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 300.px, 100.px, CropType.CENTER)

            Type.CropBottom ->
                ImageLoader().corp(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 300.px, 100.px, CropType.BOTTOM)

            Type.CropSquare ->
                ImageLoader().square(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.CropCircle ->
                ImageLoader().circle(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.ColorFilter ->
                ImageLoader().colorFilter(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, Color.argb(80, 255, 0, 0))

            Type.Grayscale ->
                ImageLoader().grayScale(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.RoundedCorners ->
                ImageLoader().rounded(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 45, 0, RoundedCornersTransformation.CornerType.BOTTOM)

            Type.Blur ->
                ImageLoader().blur(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 25)

            Type.SupportRSBlur ->
                ImageLoader().rsBlur(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 25, 10)

            Type.Toon ->
                ImageLoader().toon(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.Sepia ->
                ImageLoader().sepia(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.Contrast ->
                ImageLoader().contrast(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 2.0f)

            Type.Invert ->
                ImageLoader().invert(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.Pixel ->
                ImageLoader().pixel(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 20f)

            Type.Sketch ->
                ImageLoader().sketch(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!)

            Type.Swirl ->
                ImageLoader().swirl(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 0.5f, 1.0f, PointF(0.5f, 0.5f))

            Type.Brightness ->
                ImageLoader().brightness(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 0.5f)

            Type.Kuawahara ->
                ImageLoader().kuawahara(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, 25)

            Type.Vignette ->
                ImageLoader().vignette(holder.image, ContextCompat.getDrawable(context, R.drawable.check)!!, PointF(0.5f, 0.5f),
                        floatArrayOf(0.0f, 0.0f, 0.0f), 0f, 0.75f)
        }
        holder.title.text = dataSet[position].name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.title)
    }
}
