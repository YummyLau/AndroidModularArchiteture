package com.effective.android.component.tab.recommendation.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.effective.android.base.rxjava.RxCreator
import com.effective.android.base.rxjava.RxSchedulers
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.Sdks
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter
import com.effective.android.component.tab.recommendation.util.StringToBitmapUtils
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.tabr_holder_recommend_card_layout.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Callable

class CardView(private val imageCache: LruCache<String, Bitmap>, context: Context) : LinearLayout(context) {

    private var itemView: View = LayoutInflater.from(context).inflate(R.layout.tabr_holder_recommend_card_layout, this, true)
    var avatarDrawable: Drawable? = null
    var imageDisposable: Disposable? = null

    fun bindData(data: Article, position: Int) {
        itemView.title.text = data.title
        val avatarPath = String.format(ResourceUtils.getString(itemView.context, R.string.tabr_card_avatar_path), ((data.hashCode() % 25) + 1).toString())
        if (itemView.avatar.tag == null || !TextUtils.equals(avatarPath, itemView.avatar.tag as String)) {
            avatarDrawable = ResourceUtils.getDrawable(itemView.context, avatarPath)
            itemView.avatar.background = avatarDrawable
            itemView.avatar.tag = avatarPath
            val bitmap = imageCache[avatarPath]
            if (bitmap != null) {
                itemView.image.setImageBitmap(bitmap)
            } else {
                imageDisposable = RxCreator.createFlowable(Callable<String> { avatarPath })
                        .map { StringToBitmapUtils.createBitmapWithText(itemView.context, avatarDrawable as BitmapDrawable, data.title, 5) }
                        .compose(RxSchedulers.flowableComputationToMain())
                        .subscribe({
                            itemView.image.setImageBitmap(it)
                            imageCache.put(avatarPath, it)
                        }, {
                            itemView.image.setImageBitmap(null)
                        })
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        imageDisposable?.dispose()
        itemView.image.setImageBitmap(null)
        itemView.avatar.background = null
    }
}
