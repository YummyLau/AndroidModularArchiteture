package com.effective.android.base.view

import android.content.Context
import android.graphics.Rect
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View

import androidx.appcompat.widget.AppCompatTextView
import com.effective.android.lib_base.R


/**
 * 可扩展的textview
 * Created by yummyLau on 2018/7/02.
 * Email: yummyl.lau@gmail.com
 */
class ExpandableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var originText: CharSequence? = null
    private var callback: Callback? = null

    private var collapseLine: Int = 0
    private var collapseText: String? = null
    private var collapseTextColor: Int = 0

    private var expandLine: Int = 0
    private var expandText: String? = null
    private var expandTextColor: Int = 0

    private val needTag: Boolean = false
    private var openCheckout: Boolean = false

    private val mLastLineShowRect: Rect
    private val mLastLineActualIndexRect: Rect

    var action = Action.NONE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        if (typedArray != null) {
            collapseLine = typedArray.getInteger(R.styleable.ExpandableTextView_collapse_line, DEFAULT_COLLAPSE_LINE_COUNT)
            collapseText = typedArray.getString(R.styleable.ExpandableTextView_collapse_text)
            expandLine = typedArray.getInteger(R.styleable.ExpandableTextView_expand_line, DEFAULT_EXPAND_LINE_COUNT)
            expandText = typedArray.getString(R.styleable.ExpandableTextView_expand_text)
            openCheckout = typedArray.getBoolean(R.styleable.ExpandableTextView_open_checkout, true)
            collapseTextColor = typedArray.getResourceId(R.styleable.ExpandableTextView_collapse_color, android.R.color.holo_blue_dark)
            expandTextColor = typedArray.getResourceId(R.styleable.ExpandableTextView_expand_color, android.R.color.holo_blue_dark)
            typedArray.recycle()
        }

        if (!TextUtils.isEmpty(collapseText) && needTag) {
            collapseText = "<$collapseText>"
        }

        if (!TextUtils.isEmpty(expandText) && needTag) {
            expandText = "<$expandText>"
        }

        if (TextUtils.isEmpty(collapseText)) {
            collapseText = DEFAULT_COLLAPSE_TEXT
        }

        if (TextUtils.isEmpty(expandText)) {
            expandText = DEFAULT_EXPAND_TEXT
        }

        mLastLineShowRect = Rect()
        mLastLineActualIndexRect = Rect()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (TextUtils.isEmpty(originText)) {
            text = originText
            onStatus(Action.NONE)
            return
        }
        val staticLayout = StaticLayout(originText, paint, measuredWidth - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
        val originLineCount = staticLayout.lineCount
        if (originLineCount > collapseLine) {
            when (action) {
                Action.EXPAND -> {

                    /**
                     * 1. 如果展开状态中，originLineCount比expandLine小，则直接拼接 expandText,如果拼接之后溢出，则需要考虑 expandText 换行
                     * 2. 如果展开状态中，originLineCount大于等于expandLine，则拼接 ELLIPSIZE_END_TEXT + expandText
                     */
                    //可用宽度
                    val availableWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
                    val builder: SpannableStringBuilder

                    if (originLineCount < expandLine) {
                        val lineStart = staticLayout.getLineStart(originLineCount - 1)
                        val lineEnd = staticLayout.getLineEnd(originLineCount - 1)
                        val ellipsizedText = staticLayout.text.subSequence(0, lineEnd)
                        builder = SpannableStringBuilder(ellipsizedText)
                        builder.append(expandText)
                        val appendWidth = staticLayout.paint.measureText(builder, lineStart, builder.length)
                        if (availableWidth < appendWidth) {
                            builder.delete(ellipsizedText.length, builder.length)
                            builder.append("\n")
                            builder.append(expandText)
                        }
                    } else {
                        val limitLine = expandLine
                        val lineStart = staticLayout.getLineStart(limitLine - 1)
                        val lineEnd = staticLayout.getLineEnd(limitLine - 1)
                        val ellipsizedText = staticLayout.text.subSequence(0, lineEnd)
                        builder = SpannableStringBuilder(ellipsizedText)
                        builder.append(ELLIPSIZE_END_TEXT)
                        builder.append(expandText)
                        var appendWidth = staticLayout.paint.measureText(builder, lineStart, builder.length)
                        while (appendWidth > availableWidth) {
                            val deleteIndex = builder.length - 1 - expandText!!.length - ELLIPSIZE_END_TEXT.length
                            builder.delete(deleteIndex, deleteIndex + 1)
                            appendWidth = paint.measureText(builder, lineStart, builder.length)
                        }
                    }
                    val spanEnd = builder.length
                    val spanStart = spanEnd - expandText!!.length
                    builder.setSpan(
                            object : ClickableSpan() {
                                override fun updateDrawState(ds: TextPaint) {
                                    ds.color = resources.getColor(expandTextColor)
                                    ds.isUnderlineText = false
                                }

                                override fun onClick(widget: View) {
                                    if (openCheckout) {
                                        setText(originText, false, callback)
                                    }
                                }
                            }, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                    text = builder
                    onStatus(Action.EXPAND)
                }
                Action.COLLAPSE -> {

                    val lineStart = staticLayout.getLineStart(collapseLine - 1)
                    val lineEnd = staticLayout.getLineEnd(collapseLine - 1)
                    val ellipsizedText = staticLayout.text.subSequence(0, lineEnd)

                    //可用宽度
                    val availableWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
                    val builder = SpannableStringBuilder(ellipsizedText)
                    builder.append(ELLIPSIZE_END_TEXT)
                    builder.append(collapseText)

                    var appendWidth = staticLayout.paint.measureText(builder, lineStart, builder.length)

                    while (appendWidth > availableWidth) {
                        val deleteIndex = builder.length - 1 - collapseText!!.length - ELLIPSIZE_END_TEXT.length
                        builder.delete(deleteIndex, deleteIndex + 1)
                        appendWidth = paint.measureText(builder, lineStart, builder.length)
                    }

                    val spanEnd = builder.length
                    val spanStart = spanEnd - collapseText!!.length
                    builder.setSpan(
                            object : ClickableSpan() {
                                override fun updateDrawState(ds: TextPaint) {
                                    ds.color = resources.getColor(collapseTextColor)
                                    ds.isUnderlineText = false
                                }

                                override fun onClick(widget: View) {
                                    if (openCheckout) {
                                        setText(originText, true, callback)
                                    }
                                }
                            }, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                    text = builder
                    onStatus(Action.COLLAPSE)
                }
                Action.NONE -> {
                    text = originText
                    onStatus(Action.NONE)
                }
            }
        } else {
            text = originText
            onStatus(Action.NONE)
        }
    }

    fun onStatus(action: Action) {
        this.action = action
        if (callback != null) {
            callback!!.onAction(action)
        }
    }

    fun setDefaultText(text: CharSequence) {
        setText(text, false, null)
    }

    fun setText(text: CharSequence?, toExpand: Boolean, callback: Callback?) {
        this.originText = text
        if (toExpand) {
            action = Action.EXPAND
        } else {
            action = Action.COLLAPSE
        }
        this.callback = callback
        setText(text)
        requestLayout()
    }

    companion object {

        private val ELLIPSIZE_END_TEXT = "..."
        private val DEFAULT_COLLAPSE_LINE_COUNT = 1
        private val DEFAULT_COLLAPSE_TEXT = "<展开>"
        private val DEFAULT_EXPAND_LINE_COUNT = Integer.MAX_VALUE
        private val DEFAULT_EXPAND_TEXT = "<收起>"
    }
}

enum class Action {
    NONE,
    EXPAND,
    COLLAPSE
}

interface Callback {
    fun onAction(action: Action)
}
