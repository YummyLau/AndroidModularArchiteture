package com.effective.android.base.view.list.interfaces

import android.view.View
import androidx.annotation.NonNull
import com.effective.android.base.view.list.MediaType

interface MediaItem {

    fun onSelected()

    fun onUnselected()

    @NonNull
    fun itemKey(): String

    fun itemType(): MediaType

    fun measureView(): View
}