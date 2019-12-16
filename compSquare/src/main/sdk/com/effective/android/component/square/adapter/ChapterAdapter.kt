package com.effective.android.component.square.adapter

import android.view.View
import com.effective.android.component.square.bean.SelectableChapter
import com.effective.android.component.square.listener.OnEditListener

interface ChapterAdapter{

    fun bindData(done: List<SelectableChapter>, todo: List<SelectableChapter>, onEditListener: OnEditListener? = null): View

}