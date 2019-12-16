package com.effective.android.component.square.bean

import com.effective.android.base.view.tagdrag.IDraggable

class DraggableChapter : SelectableChapter, IDraggable {

    constructor(chapter: Chapter) : super(chapter)

    constructor(selectableChapter: SelectableChapter) : super(selectableChapter.data){
        selected = selectableChapter.selected
        draggable = selectableChapter.draggable
    }

    override fun isDraggable(): Boolean {
        return draggable
    }
}
