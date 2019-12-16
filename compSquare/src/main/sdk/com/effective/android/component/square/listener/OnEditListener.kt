package com.effective.android.component.square.listener

import com.effective.android.component.square.bean.SelectableChapter

interface OnEditListener {

    fun onEdit()

    fun onFinish(done: List<SelectableChapter>, todo: List<SelectableChapter>)
}