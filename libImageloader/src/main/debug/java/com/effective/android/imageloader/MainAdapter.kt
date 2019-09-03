package com.effective.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.Log
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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import com.effective.android.imageloader.progress.DispatchingProgressManager
import com.effective.android.imageloader.progress.lintener.UIonProgressListener
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.CropTransformation.CropType

/**
 * Created by Wasabeef on 2015/01/11.
 */
class MainAdapter(private val context: Context) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val mask = "遮罩"
    private val ninePatchMask = "泡泡遮罩"
    private val cropTop = "顶部裁剪"
    private val cropCenter = "中间裁剪"
    private val cropBottom = "底部裁剪"
    private val cropSquare = "正方形裁剪"
    private val cropCircle = "圆形裁剪"
    private val colorFilter = "颜色过滤"
    private val grayScale = "灰階"
    private val roundedCorners = "圆角"
    private val blur = "高斯模糊"
    private val supportRSBlur = "毛玻璃"
    private val toon = "卡通"
    private val sepia = "深褐色"
    private val contrast = "对比度"
    private val invert = "反色"
    private val pixel = "像素"
    private val sketch = "素描"
    private val swirl = "涡旋"
    private val brightness = "亮度"
    private val kuawahara = "水彩"
    private val vignette = "暈影"


    private var dataSet = mutableListOf<ImageType>().apply {
        val imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1553252494398&di=3658a81d13d0bce460d479054a97efa2&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201401%2F17%2F20140117103922_vQX2G.jpeg"
        add(ImageType("1", mask, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1553258280629&di=fefaa65c220324f7a4ae59f0f1eab4fd&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201605%2F25%2F233725ujmoo6jkkk64xesu.jpg"))
        add(ImageType("2", ninePatchMask, imageUrl))
        add(ImageType("3", cropTop, imageUrl))
        add(ImageType("4", cropCenter, imageUrl))
        add(ImageType("5", cropBottom, imageUrl))
        add(ImageType("6", cropSquare, imageUrl))
        add(ImageType("7", cropCircle, imageUrl))
        add(ImageType("8", colorFilter, imageUrl))
        add(ImageType("9", grayScale, imageUrl))
        add(ImageType("10", roundedCorners, imageUrl))
        add(ImageType("11", blur, imageUrl))
        add(ImageType("12", supportRSBlur, imageUrl))
        add(ImageType("13", toon, imageUrl))
        add(ImageType("14", sepia, imageUrl))
        add(ImageType("15", contrast, imageUrl))
        add(ImageType("16", invert, imageUrl))
        add(ImageType("17", pixel, imageUrl))
        add(ImageType("18", sketch, imageUrl))
        add(ImageType("19", swirl, imageUrl))
        add(ImageType("20", brightness, imageUrl))
        add(ImageType("21", kuawahara, imageUrl))
        add(ImageType("22", vignette, imageUrl))
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val data = dataSet[position]
        holder.progress.visibility = View.GONE

        when (data.type) {

            mask -> {
//                holder.progress.visibility = View.VISIBLE
//                val requestListener = object : RequestListener<Drawable> {
//
//                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                        DispatchingProgressManager.forget(data.url)
////                        holder.progress.visibility = View.GONE
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                        DispatchingProgressManager.forget(data.url)
////                        holder.progress.visibility = View.GONE
//                        return false
//                    }
//                }
//
//                DispatchingProgressManager.expect(data.url, object : UIonProgressListener {
//
//                    override fun getGranualityPercentage(): Float = 1.toFloat()
//
//                    override fun onProgress(byteRead: Long, expectedLength: Long) {
//                        val progress = byteRead * 100.toFloat() / expectedLength
//                        Log.d("progress", progress.toString())
//                        holder.progress.setProgress(progress)
//                    }
//                })
//
//                val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
//                ImageLoader().mask(holder.image, data.url, R.drawable.mask_starfish, requestOptions, requestListener)


                val gifDrawable = ContextCompat.getDrawable(context, R.drawable.test)
                var context = Utils.isValidContextForGlide(holder.image)
                if (context != null) {
                    Glide.with(holder.image.context)
                            .asDrawable()

                            .load(gifDrawable)    // does not take effect.
//                            .load(R.drawable.test)  //  in effect
                            .apply(bitmapTransform(MultiTransformation<Bitmap>(CenterCrop(), MaskTransformation(R.drawable.mask_starfish))))
                            .into(holder.image)
                }
            }

            ninePatchMask ->
                ImageLoader().mask(holder.image, data.url, R.drawable.mask_chat_right)

            cropTop ->
                ImageLoader().corp(holder.image, data.url, 300.px, 100.px, CropType.TOP)

            cropCenter ->
                ImageLoader().corp(holder.image, data.url, 300.px, 100.px, CropType.CENTER)

            cropBottom ->
                ImageLoader().corp(holder.image, data.url, 300.px, 100.px, CropType.BOTTOM)

            cropSquare ->
                ImageLoader().square(holder.image, data.url)

            cropCircle ->
                ImageLoader().circle(holder.image, data.url)

            colorFilter ->
                ImageLoader().colorFilter(holder.image, data.url, Color.argb(80, 255, 0, 0))

            grayScale ->
                ImageLoader().grayScale(holder.image, data.url)

            roundedCorners ->
                ImageLoader().rounded(holder.image, data.url, 45, 0, RoundedCornersTransformation.CornerType.BOTTOM)

            blur ->
                ImageLoader().blur(holder.image, data.url, 25)

            supportRSBlur ->
                ImageLoader().rsBlur(holder.image, data.url, 25, 10)

            toon ->
                ImageLoader().toon(holder.image, data.url)

            sepia ->
                ImageLoader().sepia(holder.image, data.url)

            contrast ->
                ImageLoader().contrast(holder.image, data.url, 2.0f)

            invert ->
                ImageLoader().invert(holder.image, data.url)

            pixel ->
                ImageLoader().pixel(holder.image, data.url, 20f)

            sketch ->
                ImageLoader().sketch(holder.image, data.url)

            swirl ->
                ImageLoader().swirl(holder.image, data.url, 0.5f, 1.0f, PointF(0.5f, 0.5f))

            brightness ->
                ImageLoader().brightness(holder.image, data.url, 0.5f)

            kuawahara ->
                ImageLoader().kuawahara(holder.image, data.url, 25)

            vignette ->
                ImageLoader().vignette(holder.image, data.url, PointF(0.5f, 0.5f),
                        floatArrayOf(0.0f, 0.0f, 0.0f), 0f, 0.75f)
        }
        holder.title.text = dataSet[position].type
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.title)
        var progress: ImageProgressView = itemView.findViewById(R.id.progress)
    }
}
