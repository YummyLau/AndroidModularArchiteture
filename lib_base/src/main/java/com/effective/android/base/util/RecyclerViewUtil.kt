package com.effective.android.base.util

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView


object RecyclerViewUtil {

    fun disableDefaultAnimations(recyclerView: RecyclerView) {
        val itemAnimator = recyclerView.getItemAnimator()
        if (itemAnimator is DefaultItemAnimator) {
            val defaultItemAnimator = itemAnimator as DefaultItemAnimator
            defaultItemAnimator.addDuration = 0
            defaultItemAnimator.changeDuration = 0
            defaultItemAnimator.moveDuration = 0
            defaultItemAnimator.removeDuration = 0
            defaultItemAnimator.supportsChangeAnimations = false
        }
    }
}
