package com.effective.android.base.view.list.interfaces

interface ILifeItem {

    fun onViewAttach()

    fun onViewDetach()

    fun onViewRecycled()

    fun onScrollStatus(isScrolling: Boolean)
}