package com.effective.android.base.view.list.interfaces

import android.view.View
import androidx.annotation.NonNull
import com.effective.android.base.view.list.anno.MediaType

interface IMediaItem {

    fun onSelected()

    fun onUnselected()

    @NonNull
    fun itemKey(): String

    fun itemType(): MediaType

    fun measureView(): View
}