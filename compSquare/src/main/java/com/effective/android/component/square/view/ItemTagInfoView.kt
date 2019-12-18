package com.effective.android.component.square.view


import android.annotation.TargetApi
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.effective.android.component.square.R
import com.effective.android.component.square.bean.BlogArticle
import kotlinx.android.synthetic.main.square_item_tag_layout.view.*

/**
 * 卡片 标签view
 * created by yummylau on 2019/12/12
 */
class ItemTagInfoView : LinearLayout {

    private val root: View = LayoutInflater.from(context).inflate(R.layout.square_item_tag_layout, this, true)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    fun bindData(data: BlogArticle) {
        val superChapterName = data.superChapterName
        val chapterName = data.chapterName
        if (TextUtils.isEmpty(superChapterName) && TextUtils.isEmpty(chapterName)) {
            visibility = View.GONE
            return
        }
        visibility = View.VISIBLE

        root.superChapterNameViewContainer.visibility = if (!TextUtils.isEmpty(superChapterName)) {
            superChapterNameView.text = superChapterName
            View.VISIBLE
        } else {
            View.GONE
        }

        root.chapterNameViewContainer.visibility = if (!TextUtils.isEmpty(superChapterName)) {
            chapterNameView.text = chapterName
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
