package com.effective.android.component.square.view

import android.content.Context
import android.graphics.Typeface.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.effective.android.base.util.DisplayUtils
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.view.dragable.tag.ClickToDeleteItemListenerImpl
import com.effective.android.base.view.dragable.tag.DragAdapter
import com.effective.android.base.view.dragable.tag.DragFlowLayout
import com.effective.android.component.square.R
import com.effective.android.component.square.bean.DraggableChapter
import com.effective.android.component.square.bean.SelectableChapter
import com.effective.android.component.square.listener.OnEditListener
import skin.support.widget.SkinCompatScrollView


/**
 * 章节选择器，用于用户自主选择章节，可复用于任何文章列表
 * created by yummylau on 2019/12/16
 */
class ChapterSelectView : SkinCompatScrollView {

    private lateinit var container: RelativeLayout
    private lateinit var cancel: ImageView
    private lateinit var dontTitleView: TextView
    private lateinit var dontDragFlowLayout: DragFlowLayout
    private lateinit var todoTitleView: TextView
    private lateinit var todoDragFlowLayout: DragFlowLayout
    lateinit var action: TextView
    private var listenter: OnEditListener? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    override fun applySkin() {
        super.applySkin()
        setBackgroundColor(ResourceUtils.getColor(context,R.color.windowBackground))
        cancel.setImageDrawable(ResourceUtils.getDrawable(context, R.drawable.square_ic_chapter_selected_cancel))
        action.setTextColor(ResourceUtils.getColor(context, R.color.colorThemeText))
        action.background = ResourceUtils.getDrawable(context, R.drawable.square_se_chatper_selector_action)
        dontTitleView.setTextColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        todoTitleView.setTextColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
    }

    private fun initView(context: Context) {

        setBackgroundColor(ResourceUtils.getColor(context,R.color.windowBackground))

        container = RelativeLayout(context)
        val containerLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(container, containerLp)

        cancel = ImageView(context)
        cancel.id = View.generateViewId()
        cancel.setImageDrawable(ResourceUtils.getDrawable(context, R.drawable.square_ic_chapter_selected_cancel))
        cancel.setPadding(DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 10f))
        val cancelLp = LayoutParams(DisplayUtils.dip2px(context, 40f), DisplayUtils.dip2px(context, 40f))
        container.addView(cancel, cancelLp)
        cancel.setOnClickListener {
            visibility = View.GONE
            listenter?.onCancel()
        }

        action = TextView(context)
        action.id = View.generateViewId()
        action.textSize = 13f
        action.setTextColor(ResourceUtils.getColor(context, R.color.colorThemeText))
        action.text = ResourceUtils.getString(context, R.string.square_edit)
        action.background = ResourceUtils.getDrawable(context, R.drawable.square_se_chatper_selector_action)
        action.setPadding(DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 5f), DisplayUtils.dip2px(context, 10f), DisplayUtils.dip2px(context, 5f))
        val actionLp = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        actionLp.topMargin = DisplayUtils.dip2px(context, 5f)
        actionLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        action.setOnClickListener {
            if (action.text == context.getString(R.string.square_edit)) {
                dontDragFlowLayout.beginDrag()
                action.text = context.getString(R.string.square_finish)
                listenter?.onEdit()
            } else {
                dontDragFlowLayout.finishDrag()
                action.text = context.getString(R.string.square_edit)
                visibility = View.GONE
                listenter?.onFinish(dontDragFlowLayout.dragItemManager.getItems(), todoDragFlowLayout.dragItemManager.getItems())
            }
        }
        container.addView(action, actionLp)

        dontTitleView = TextView(context)
        dontTitleView.id = View.generateViewId()
        dontTitleView.textSize = 15f
        dontTitleView.setTextColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        dontTitleView.typeface = defaultFromStyle(BOLD)
        dontTitleView.text = ResourceUtils.getString(context, R.string.square_list_edit_done)
        val dontTitleViewLp = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        dontTitleViewLp.addRule(RelativeLayout.BELOW, cancel.id)
        dontTitleViewLp.topMargin = DisplayUtils.dip2px(context, 5f)
        container.addView(dontTitleView, dontTitleViewLp)

        dontDragFlowLayout = DragFlowLayout(context)
        dontDragFlowLayout.id = View.generateViewId()
        val dontDragFlowLayoutLp = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        dontDragFlowLayoutLp.topMargin = DisplayUtils.dip2px(context, 10f)
        dontDragFlowLayoutLp.addRule(RelativeLayout.BELOW, dontTitleView.id)
        dontDragFlowLayout.lineSpacing = DisplayUtils.dip2px(context, 5f)
        dontDragFlowLayout.itemSpacing = DisplayUtils.dip2px(context, 5f)
        dontDragFlowLayout.setOnDragStateChangeListener { dfl, dragState ->
            action.text = if (dragState == DragFlowLayout.DRAG_STATE_IDLE) {
                context.getString(R.string.square_edit)
            } else {
                listenter?.onEdit()
                context.getString(R.string.square_finish)
            }
            action.isEnabled = dragState != DragFlowLayout.DRAG_STATE_DRAGGING
        }
        dontDragFlowLayout.setOnItemClickListener(object : ClickToDeleteItemListenerImpl(R.id.close) {

            override fun performClick(dragFlowLayout: DragFlowLayout?, child: View?, event: MotionEvent?, dragState: Int): Boolean {
                return super.performClick(dragFlowLayout, child, event, dragState)
            }

            override fun onDeleteSuccess(dfl: DragFlowLayout?, child: View?, data: Any?) {
                super.onDeleteSuccess(dfl, child, data)
                val selectableChapter = child?.tag as DraggableChapter
                if (selectableChapter.selected) {
                    selectableChapter.selected = false
                }
                todoDragFlowLayout.dragItemManager.addItem(data)
            }
        })
        dontDragFlowLayout.setDragAdapter(object : DragAdapter<DraggableChapter>() {

            override fun getItemLayoutId(): Int = R.layout.square_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: DraggableChapter?) {
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.data?.name
                val closeView = itemView?.findViewById(R.id.close) as View
                if (dragState != DragFlowLayout.DRAG_STATE_IDLE && data?.draggable!!) {
                    closeView.visibility = View.VISIBLE
                } else {
                    closeView.visibility = View.GONE
                }
                if (data?.draggable!!) {
                    itemView.isSelected = true
                    tv.isSelected = true
                } else {
                    itemView.isSelected = false
                    tv.isSelected = false
                }
            }

            override fun getData(itemView: View?): DraggableChapter = itemView?.tag as DraggableChapter
        })
        container.addView(dontDragFlowLayout, dontDragFlowLayoutLp)

        todoTitleView = TextView(context)
        todoTitleView.id = View.generateViewId()
        todoTitleView.textSize = 15f
        todoTitleView.setTextColor(ResourceUtils.getColor(context, R.color.colorTextPrimary))
        todoTitleView.typeface = defaultFromStyle(BOLD)
        todoTitleView.text = ResourceUtils.getString(context, R.string.square_list_to_edit)
        val todoTitleViewLp = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        todoTitleViewLp.topMargin = DisplayUtils.dip2px(context, 10f)
        todoTitleViewLp.addRule(RelativeLayout.BELOW, dontDragFlowLayout.id)
        container.addView(todoTitleView, todoTitleViewLp)


        todoDragFlowLayout = DragFlowLayout(context)
        todoDragFlowLayout.id = View.generateViewId()
        val todoDragFlowLayoutLp = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        todoDragFlowLayoutLp.topMargin = DisplayUtils.dip2px(context, 10f)
        todoDragFlowLayoutLp.addRule(RelativeLayout.BELOW, todoTitleView.id)
        todoDragFlowLayout.lineSpacing = DisplayUtils.dip2px(context, 5f)
        todoDragFlowLayout.itemSpacing = DisplayUtils.dip2px(context, 5f)
        todoDragFlowLayout.setDragAdapter(object : DragAdapter<DraggableChapter>() {

            override fun getItemLayoutId(): Int = R.layout.square_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: DraggableChapter?) {
                itemView?.isSelected = false
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.isSelected = false
                tv.text = data?.data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView.findViewById(R.id.close) as View
                closeView.visibility = View.INVISIBLE
                itemView.setOnClickListener {
                    if (data != null && !data.selected) {
                        data.selected = true
                        if (dontDragFlowLayout.dragState != DragFlowLayout.DRAG_STATE_DRAGGABLE) {
                            dontDragFlowLayout.beginDrag()
                        }
                        todoDragFlowLayout.dragItemManager.removeItem(data)
                        dontDragFlowLayout.dragItemManager.addItem(data)
                        action.text = context.getString(R.string.square_finish)
                    }
                }
            }

            override fun getData(itemView: View?): DraggableChapter = itemView?.tag as DraggableChapter
        })
        todoDragFlowLayout.setDraggable(false)
        container.addView(todoDragFlowLayout, todoDragFlowLayoutLp)

        setOnClickListener {
            //默认拦截
        }

        val padding = DisplayUtils.dip2px(context, 10f)
        isFillViewport = true
        container.setPadding(padding, padding, padding, padding)
    }

    fun bindData(done: List<SelectableChapter>, todo: List<SelectableChapter>, onEditListener: OnEditListener? = null) {
        for (item in done) {
            dontDragFlowLayout.dragItemManager.addItem(DraggableChapter(item))
        }
        for (item in todo) {
            todoDragFlowLayout.dragItemManager.addItem(DraggableChapter(item))
        }
        this.listenter = onEditListener
    }

}
