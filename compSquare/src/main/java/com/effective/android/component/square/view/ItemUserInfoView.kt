package com.effective.android.component.square.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.effective.android.base.util.DisplayUtils
import com.effective.android.base.util.ResourceUtils
import com.effective.android.component.square.R
import com.effective.android.component.square.bean.BlogArticle
import java.util.*

/**
 * 卡片 用户信息实体
 * created by yummylau on 2019/12/12
 */
class ItemUserInfoView : RelativeLayout {

    private lateinit var avatarView: View
    private lateinit var nickView: TextView
    private lateinit var createTime: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        avatarView = View(context)
        avatarView.id = View.generateViewId()
        val avatarViewLp = LayoutParams(DisplayUtils.dip2px(context, 38f), DisplayUtils.dip2px(context, 38f))
        avatarView.background = ResourceUtils.getDrawable(context, R.drawable.blog_sh_avatar)
        avatarViewLp.marginEnd = DisplayUtils.dip2px(context, 10f)
        addView(avatarView, avatarViewLp)

        nickView = TextView(context)
        nickView.id = View.generateViewId()
        nickView.includeFontPadding = false
        nickView.textSize = 15f
        nickView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        nickView.setTextColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        val nickViewLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        nickViewLp.addRule(ALIGN_TOP, avatarView.id)
        nickViewLp.addRule(RIGHT_OF, avatarView.id)
        addView(nickView, nickViewLp)

        createTime = TextView(context)
        createTime.id = View.generateViewId()
        createTime.includeFontPadding = false
        createTime.textSize = 10f
        createTime.setTextColor(ResourceUtils.getColor(context, R.color.colorTextSecondary))
        val createTimeLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        createTimeLp.addRule(ALIGN_BOTTOM, avatarView.id)
        createTimeLp.addRule(RIGHT_OF, avatarView.id)
        createTimeLp.bottomMargin = DisplayUtils.dip2px(context, 4f)
        addView(createTime, createTimeLp)
    }

    fun bindData(data: BlogArticle) {
        val avatarPath = String.format(ResourceUtils.getString(context,R.string.blog_card_avatar_path),(Random().nextInt(25) + 1).toString())
        avatarView.background = ResourceUtils.getDrawable(context, avatarPath)
        nickView.text =  if(!TextUtils.isEmpty(data.author)){
            createTime.text =  String.format(ResourceUtils.getString(context,R.string.blog_card_create_time_prefix),data.niceDate)
            data.author
        }else{
            createTime.text =  String.format(ResourceUtils.getString(context,R.string.blog_card_share_time_prefix),data.niceDate)
            data.shareUser
        }
    }
}
