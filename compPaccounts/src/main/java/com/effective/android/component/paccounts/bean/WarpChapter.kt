package com.effective.android.component.paccounts.bean

import com.effective.android.base.view.tagdrag.IDraggable
import com.effective.android.component.square.bean.Chapter

class WarpChapter(val data: Chapter) : IDraggable {

    //是否选择
    var selected = true

    //是否支持拖拽
    var draggable = true

    override fun isDraggable(): Boolean = draggable
}