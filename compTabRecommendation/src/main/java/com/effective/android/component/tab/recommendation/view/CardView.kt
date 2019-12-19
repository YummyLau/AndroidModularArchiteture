package com.effective.android.component.tab.recommendation.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
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
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.Sdks
import io.reactivex.disposables.Disposable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.tabr_holder_recommend_card_layout.view.*

class CardView(context: Context) : LinearLayout(context) {

    companion object {
        private val imageCache: LruCache<String, Drawable> = LruCache(10)
        private var imageIndex = 1
    }

    private var itemView: View = LayoutInflater.from(context).inflate(R.layout.tabr_holder_recommend_card_layout, this, true)
    var avatarDrawable: Drawable? = null

    fun bindData(data: Article, position: Int) {
        itemView.title.text = data.title
        itemView.infoContainer.background = ResourceUtils.getDrawable(context, R.drawable.tabr_sh_card_content_bg)
        avatarDrawable = imageCache.get(data.title)
        if (avatarDrawable == null) {
            if (TextUtils.isEmpty(data.envelopePic)) {
                loadImageByLocal(data)
            } else {
                loadImageByServer(data)
            }
        } else {
            itemView.image.setImageDrawable(avatarDrawable)
        }
        itemView.cardTag.text = if (TextUtils.isEmpty(data.chapterName)) {
            if (TextUtils.isEmpty(data.superChapterName)) {
                ResourceUtils.getString(context, R.string.tabr_recommend_card_tip)
            } else {
                data.superChapterName
            }
        } else {
            data.chapterName
        }
    }

    private fun loadImageByLocal(data: Article) {
        avatarDrawable = ResourceUtils.getDrawable(itemView.context,
                String.format(ResourceUtils.getString(itemView.context, R.string.tabr_ic_card_image), imageIndex.toString()))
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
        val requestListener = object : RequestListener<Drawable> {

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                avatarDrawable = null
                itemView.image.setImageDrawable(avatarDrawable)
                imageCache.put(data.title, avatarDrawable)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                avatarDrawable = resource
                itemView.image.setImageDrawable(avatarDrawable)
                imageCache.put(data.title, avatarDrawable)
                return true
            }
        }
        Sdks.serviceImageloder.rounded(itemView.image,
                avatarDrawable!!, 10, 0, RoundedCornersTransformation.CornerType.TOP, requestOptions, requestListener)
        if (imageIndex == 30) {
            imageIndex = 1
        } else {
            imageIndex++
        }
    }

    private fun loadImageByServer(data: Article) {
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
        val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                avatarDrawable = null
                loadImageByLocal(data)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                avatarDrawable = resource
                itemView.image.setImageDrawable(avatarDrawable)
                imageCache.put(data.title, avatarDrawable)
                return true
            }
        }
        Sdks.serviceImageloder.rounded(itemView.image,
                data.envelopePic, 10, 0, RoundedCornersTransformation.CornerType.TOP, requestOptions, requestListener)
    }

}
