package com.effective.android.component.tab.recommendation.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.LruCache
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.view.dragable.card.TouchFromParent
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.Sdks
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.tabr_holder_recommend_card_layout.view.*
import kotlin.math.abs

class CardView(context: Context) : LinearLayout(context), TouchFromParent {

    companion object {
        private val imageCache: LruCache<String, Drawable> = LruCache(10)
        private var imageIndex = 1
    }

    private var itemView: View = LayoutInflater.from(context).inflate(R.layout.tabr_holder_recommend_card_layout, this, true)
    var avatarDrawable: Drawable? = null
    var data: Article? = null

    fun bindData(data: Article, position: Int) {
        this.data = data
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

    private var toClickView: View? = null
    private var lastX = 0f // 记录上次X位置
    private var lastY = 0f // 记录上次Y位置


    private fun getTouchInTargetView(event: MotionEvent): View? {
        val x = event.rawX
        val y = event.rawY
        val rect = Rect()
        getGlobalVisibleRect(rect)
        return if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            this
        } else null
    }

    override fun onTouch(v: View?, event: MotionEvent) {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                toClickView = getTouchInTargetView(event)
                return
            }
            MotionEvent.ACTION_MOVE -> {
                if (toClickView != null && abs(x - lastX) > abs(y - lastY)) {
                    toClickView = null
                }
                return
            }
            MotionEvent.ACTION_UP -> {
                if (toClickView != null && data != null) {
                    Sdks.getSdk().gotoDetailActivity(context, data!!)
                    toClickView = null
                }
                return
            }
        }
    }
}
