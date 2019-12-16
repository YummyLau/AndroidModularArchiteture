package com.effective.android.component.paccounts.view

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.paccounts.R
import com.effective.android.component.paccounts.vm.PaccountsViewModel
import com.effective.android.component.square.bean.Chapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.paccounts_fragment_laout.*
import android.widget.TextView

import com.effective.android.base.view.tagdrag.ClickToDeleteItemListenerImpl
import com.effective.android.base.view.tagdrag.DragAdapter
import com.effective.android.base.view.tagdrag.DragFlowLayout
import com.effective.android.base.view.tagdrag.DragFlowLayout.DRAG_STATE_DRAGGING
import com.effective.android.base.view.tagdrag.DragFlowLayout.DRAG_STATE_IDLE
import com.effective.android.component.paccounts.bean.WarpChapter


class PaccountsFragment : BaseVmFragment<PaccountsViewModel>() {

    private var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()
    var projects: List<Chapter>? = null
    var tags: MutableList<WarpChapter> = mutableListOf()
    val defaultSelectedSize = 5
    val defaultDisableDragIndex = 0

    override fun getViewModel(): Class<PaccountsViewModel> = PaccountsViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.paccounts_fragment_laout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageState.toLoading("正在加载")
        initView()
        fetchData()
    }

    private fun initView() {
        tabMore.setOnClickListener {
            dragContainer.visibility = View.VISIBLE
        }
        dragContainer.setOnClickListener {

        }
        dragLayoutDone.setOnItemClickListener(object : ClickToDeleteItemListenerImpl(R.id.close) {

            override fun performClick(dragFlowLayout: DragFlowLayout?, child: View?, event: MotionEvent?, dragState: Int): Boolean {
                return super.performClick(dragFlowLayout, child, event, dragState)
            }

            override fun onDeleteSuccess(dfl: DragFlowLayout?, child: View?, data: Any?) {
                super.onDeleteSuccess(dfl, child, data)
                val warpChapter = child?.tag as WarpChapter
                if (warpChapter.selected) {
                    warpChapter.selected = false
                }
                dragLayoutTodo.dragItemManager.addItem(data)
            }
        })
        dragLayoutDone.setDragAdapter(object : DragAdapter<WarpChapter>() {

            override fun getItemLayoutId(): Int = R.layout.paccounts_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: WarpChapter?) {
                itemView?.isSelected = true
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView?.findViewById(R.id.close) as View
                closeView.visibility = if (dragState !== DragFlowLayout.DRAG_STATE_IDLE && data?.draggable!!)
                    View.VISIBLE
                else
                    View.INVISIBLE
            }

            override fun getData(itemView: View?): WarpChapter = itemView?.tag as WarpChapter
        })
        dragLayoutDone.setOnDragStateChangeListener { dfl, dragState ->
            finish.text = if (dragState == DRAG_STATE_IDLE) {
                context?.getString(R.string.paccounts_edit)
            } else {
                context?.getString(R.string.paccounts_finfish)
            }
            finish.isEnabled = dragState != DRAG_STATE_DRAGGING
        }

        finish.setOnClickListener {
            if (dragLayoutDone.dragState == DRAG_STATE_IDLE) {
                dragLayoutDone.beginDrag()
                finish.text = context?.getString(R.string.paccounts_finfish)
            } else {
                dragLayoutDone.finishDrag()
                finish.text = context?.getString(R.string.paccounts_edit)
                dragContainer.visibility = View.GONE
                initPagerData(dragLayoutDone.dragItemManager.getItems())
            }
        }

        dragLayoutTodo.setDragAdapter(object : DragAdapter<WarpChapter>() {

            override fun getItemLayoutId(): Int = R.layout.paccounts_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: WarpChapter?) {
                itemView?.isSelected = false
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView?.findViewById(R.id.close) as View
                closeView.visibility = View.INVISIBLE
                itemView.setOnClickListener {
                    if (data != null && !data.selected) {
                        data?.selected = true
                        dragLayoutTodo.dragItemManager.removeItem(data)
                        dragLayoutDone.dragItemManager.addItem(data)
                    }
                }
            }

            override fun getData(itemView: View?): WarpChapter = itemView?.tag as WarpChapter
        })
        dragLayoutTodo.setDraggable(false)
    }

    private fun fetchData() {
        pageState.visibility = View.VISIBLE
        fetchProjectDisposable = viewModel.getPaccounts()
                .subscribe({
                    if (it.isSuccess) {
                        if (!it.data.isNullOrEmpty()) {
                            pageState.visibility = View.GONE
                            projects = it.data!!
                            initDragData(projects!!)
                        } else {
                            pageState.toEmpty("当前页面没有项目", "尝试刷新", Runnable {
                                pageState.toLoading("正在加载")
                                fetchData()
                            })
                        }
                    } else {
                        pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                            pageState.toLoading("正在加载")
                            fetchData()
                        })
                    }

                }, {
                    pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                        pageState.toLoading("正在加载")
                        fetchData()
                    })
                })
    }

    private fun initDragData(data: List<Chapter>) {
        for ((startIndex, item) in data.withIndex()) {
            val warpChapter = WarpChapter(item)
            warpChapter.draggable = startIndex != defaultDisableDragIndex
            warpChapter.selected = startIndex < defaultSelectedSize
            tags.add(warpChapter)
            dragLayoutDone.dragItemManager.addItem(warpChapter)
        }
        initPagerData(dragLayoutDone.dragItemManager.getItems())
    }

    private fun initPagerData(data: List<WarpChapter>){
        //设置tab
        articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                var fragment: ArticleFragment? = articleFragments[position]
                if (fragment == null) {
                    fragment = ArticleFragment(data[position].data)
                    articleFragments[position] = fragment
                }
                return fragment
            }

            override fun getCount(): Int = data.size

            override fun getPageTitle(position: Int): CharSequence? = data[position].data.name
        }
        tabLayout.setupWithViewPager(articlePager)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchProjectDisposable != null && fetchProjectDisposable!!.isDisposed) {
            fetchProjectDisposable?.dispose()
        }
    }
}