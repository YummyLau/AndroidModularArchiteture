package com.effective.android.component.tab.recommendation.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.effective.android.base.rxjava.RxCreator
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.base.util.ImageUtil
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.Sdks
import com.effective.android.component.tab.recommendation.util.StringToBitmapUtils
import io.reactivex.disposables.Disposable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.tabr_holder_recommend_card_layout.view.*
import java.util.concurrent.Callable

class CardView(private val imageCache: LruCache<String, Drawable>, context: Context) : LinearLayout(context) {

    private var itemView: View = LayoutInflater.from(context).inflate(R.layout.tabr_holder_recommend_card_layout, this, true)
    var avatarDrawable: Drawable? = null
    var imageDisposable: Disposable? = null

    fun bindData(data: Article, position: Int) {
        itemView.title.text = data.title
        itemView.infoContainer.background = ResourceUtils.getDrawable(context, R.drawable.tabr_sh_card_content_bg)
        val avatarPath = String.format(ResourceUtils.getString(itemView.context, R.string.tabr_card_avatar_path), ((data.hashCode() % 25) + 1).toString())
        avatarDrawable = ResourceUtils.getDrawable(itemView.context, avatarPath)
        val drawable = imageCache[data.title]
        if (drawable != null) {
            itemView.image.setImageDrawable(drawable)
        } else {
            imageDisposable = RxCreator.createFlowable(Callable<String> { avatarPath })
                    .map { StringToBitmapUtils.createBitmapWithText(itemView.context, avatarDrawable as BitmapDrawable, data.title, 5) }
                    .compose(RxSchedulers.flowableComputationToMain())
                    .subscribe({
                        if (it != null) {
                            val cacheDrawable = ImageUtil.bitmapToDrawable(itemView.context, it)
                            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
                            val requestListener = object : RequestListener<Drawable> {

                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    itemView.image.setImageDrawable(null)
                                    return true
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    itemView.image.setImageDrawable(resource)
                                    imageCache.put(data.title, resource)
                                    return true
                                }
                            }
                            Sdks.serviceImageloder.rounded(itemView.image,
                                    cacheDrawable, 10, 0, RoundedCornersTransformation.CornerType.TOP, requestOptions, requestListener)
                        } else {
                            itemView.image.setImageDrawable(null)
                        }
                    }, {
                        itemView.image.setImageDrawable(null)
                    })
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}
