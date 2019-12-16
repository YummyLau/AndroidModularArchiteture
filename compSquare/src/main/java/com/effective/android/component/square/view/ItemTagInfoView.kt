package com.effective.android.component.square.view


import android.annotation.TargetApi
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.effective.android.base.util.DisplayUtils
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.R
import com.effective.android.component.square.bean.BlogArticle

/**
 * 卡片 标签view
 * created by yummylau on 2019/12/12
 */
class ItemTagInfoView: LinearLayout{

    private lateinit var superChapterNameView: TextView
    private lateinit var splitView: View
    private lateinit var chapterNameView: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs,0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        orientation = HORIZONTAL
        initView(context)
    }


    private fun initView(context: Context) {
        superChapterNameView = TextView(context)
        superChapterNameView.visibility = View.GONE
        superChapterNameView.id = View.generateViewId()
        superChapterNameView.includeFontPadding = false
        superChapterNameView.textSize = 12f
        superChapterNameView.background = ResourceUtils.getDrawable(context,R.drawable.square_sh_tag_super_chapter)
        superChapterNameView.setTextColor(ResourceUtils.getColor(context, R.color.colorThemeText))
        superChapterNameView.setPadding(DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 3f), DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 3f))
        val superChapterNameViewLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(superChapterNameView, superChapterNameViewLp)

        splitView = View(context)
        splitView.visibility = View.GONE
        splitView.id = View.generateViewId()
        splitView.setBackgroundColor(ResourceUtils.getColor(context,R.color.colorDivider))
        val splitViewLp = LayoutParams(DisplayUtils.dip2px(context,1f), DisplayUtils.dip2px(context,12f))
        splitViewLp.marginStart = DisplayUtils.dip2px(context, 5f)
        addView(splitView, splitViewLp)

        chapterNameView = TextView(context)
        chapterNameView.visibility = View.GONE
        chapterNameView.id = View.generateViewId()
        chapterNameView.includeFontPadding = false
        chapterNameView.textSize = 12f
        chapterNameView.background = ResourceUtils.getDrawable(context,R.drawable.square_sh_tag_chapter)
        chapterNameView.setTextColor(ResourceUtils.getColor(context, R.color.colorThemeText))
        chapterNameView.setPadding(DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 3f), DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 3f))
        val chapterNameViewLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        chapterNameViewLp.marginStart = DisplayUtils.dip2px(context, 5f)
        addView(chapterNameView, chapterNameViewLp)

        gravity = Gravity.CENTER_VERTICAL
    }

    fun bindData(data: BlogArticle) {
        val superChapterName = data.superChapterName
        val chapterName = data.chapterName
        if (TextUtils.isEmpty(superChapterName) && TextUtils.isEmpty(chapterName)) {
            visibility = View.GONE
            return
        }
        visibility = View.VISIBLE

        superChapterNameView.visibility = if (!TextUtils.isEmpty(superChapterName)) {
            superChapterNameView.text = superChapterName
            View.VISIBLE
        } else {
            View.GONE
        }

        chapterNameView.visibility = if (!TextUtils.isEmpty(superChapterName)) {
            chapterNameView.text = chapterName
            View.VISIBLE
        } else {
            View.GONE
        }
        splitView.visibility = chapterNameView.visibility
    }
}
