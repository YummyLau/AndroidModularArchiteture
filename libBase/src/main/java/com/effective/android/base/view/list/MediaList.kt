package com.effective.android.base.view.list

import android.content.Context
import android.util.AttributeSet
import android.view.View

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.effective.android.base.util.DisplayUtils
import com.effective.android.base.view.list.listener.ImageScrollListener
import com.effective.android.base.view.list.listener.MediaScrollListener


/**
 * 支持多媒体的列表
 * created by yummylau on 2018/4/16
 */

class MediaList @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    private var movePosition: Boolean = false
    private var targetPosition: Int = 0

    var innerLayoutManager: RecyclerView.LayoutManager? = null

    private var mediaScrollListener: MediaScrollListener? = null
    private var imageScollListener: ImageScrollListener? = null


    val isEmpty: Boolean
        get() = getLayoutManager()!!.itemCount <= 0

    init {
        innerLayoutManager = getLayoutManager()
        layoutManager = innerLayoutManager
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (innerLayoutManager is LinearLayoutManager && movePosition) {
                    val n = targetPosition - (innerLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (0 <= n && n < recyclerView.childCount) {
                        val top = recyclerView.getChildAt(n).top
                        recyclerView.smoothScrollBy(0, top - if (targetPosition == 0) 0 else DisplayUtils.dip2px(getContext(), 50f))
                    }
                }
                movePosition = false
            }
        })
    }

    fun attachMediaScrollListener(mediaScrollListener: MediaScrollListener) {
        this.mediaScrollListener = mediaScrollListener
    }

    fun attachImageScrollListener(imageScrollListener: ImageScrollListener) {
        this.imageScollListener = imageScrollListener
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        if (innerLayoutManager == null) {
            innerLayoutManager = object : LinearLayoutManager(context) {
                override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
                    return 300
                }
            }
            (innerLayoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
        return innerLayoutManager
    }


    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        mediaScrollListener?.syncMediaListVisibilityChanged(visibility)
    }

    fun onResume() {
        mediaScrollListener?.syncResume()
    }

    fun onPause() {
        mediaScrollListener?.syncPause()
    }

    fun onDestroy() {
        mediaScrollListener?.syncDestroy()
    }

    @CallSuper
    override fun onChildAttachedToWindow(child: View) {
        super.onChildAttachedToWindow(child)
        mediaScrollListener?.syncMediaListChildAttachedToWindow(child)
    }

    override fun onChildDetachedFromWindow(child: View) {
        super.onChildDetachedFromWindow(child)
        mediaScrollListener?.syncMediaListChildDetachedFromWindow(child)
    }

    fun scrollToTopCompat5Item() {
        if (innerLayoutManager is LinearLayoutManager) {
            val position = (innerLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (position > 5) {
                super.scrollToPosition(5)
                smoothScrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean) {
        if (innerLayoutManager !is LinearLayoutManager) {
            return
        }
        this.targetPosition = position
        val firstItem = (innerLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastItem = (innerLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        //然后区分情况
        if (position <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            if (smoothScroll) {
                smoothScrollToPosition(position)
                smoothScrollBy(0, 0)
            } else {
                super.scrollToPosition(position)
                scrollBy(0, 0)
            }
        } else if (position <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            val top = getChildAt(position - firstItem).top
            if (smoothScroll) {
                smoothScrollBy(0, top - if (position == 0) 0 else DisplayUtils.dip2px(context, 50f))
            } else {
                scrollBy(0, top - if (position == 0) 0 else DisplayUtils.dip2px(context, 50f))
            }
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            if (smoothScroll) {
                smoothScrollToPosition(position)
            } else {
                super.scrollToPosition(position)
            }
            //这里这个变量是用在RecyclerView滚动监听里面的
            movePosition = true
        }
    }

    fun scrollToTop() {
        val count = innerLayoutManager!!.childCount
        if (count > 0) {
            scrollToPosition(0)
        }
    }

    override fun scrollToPosition(position: Int) {
        scrollToPosition(position, true)
    }

    fun scrollToPositionOriginal(position: Int) {
        super.scrollToPosition(position)
    }

}
