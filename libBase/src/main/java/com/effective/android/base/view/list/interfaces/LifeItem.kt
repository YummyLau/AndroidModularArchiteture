package com.effective.android.base.view.list.interfaces

interface LifeItem {

    fun onViewAttach()

    fun onViewDetach()

    fun onViewRecycled()

    fun onScrollStatus(isScrolling: Boolean)
}