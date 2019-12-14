package com.effective.android.component.paccounts.view

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.paccounts.R
import com.effective.android.component.paccounts.vm.PaccountsViewModel
import com.effective.android.component.blog.bean.Chapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.paccounts_fragment_laout.*
import android.util.Log
import android.widget.TextView

import com.effective.android.base.view.tagdrag.ClickToDeleteItemListenerImpl
import com.effective.android.base.view.tagdrag.DragAdapter
import com.effective.android.base.view.tagdrag.DragFlowLayout
import com.effective.android.base.view.tagdrag.DragFlowLayout.DRAG_STATE_DRAGGING
import com.effective.android.base.view.tagdrag.DragFlowLayout.DRAG_STATE_IDLE
import com.effective.android.base.view.tagdrag.IViewObserver
import com.effective.android.component.paccounts.bean.WarpChapter


class PaccountsFragment: BaseVmFragment<PaccountsViewModel>() {

    private var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()
    var projects: List<Chapter>? = null
    var tags: MutableList<WarpChapter> = mutableListOf()

    override fun getViewModel(): Class<PaccountsViewModel> = PaccountsViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.paccounts_fragment_laout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageState.toLoading("正在加载")
        initView()
        initData()
    }

    private fun initView(){
        tabMore.setOnClickListener {
            dragContainer.visibility = View.VISIBLE
        }
    }

    private fun initDragData(data : List<Chapter>){
        dragContainer.setOnClickListener {

        }
        dragLayoutDone.setOnItemClickListener(object : ClickToDeleteItemListenerImpl(R.id.close) {

            override fun performClick(dragFlowLayout: DragFlowLayout?, child: View?, event: MotionEvent?, dragState: Int): Boolean {
                return super.performClick(dragFlowLayout, child, event, dragState)
            }

            override fun onDeleteSuccess(dfl: DragFlowLayout?, child: View?, data: Any?) {
                super.onDeleteSuccess(dfl, child, data)
                val warpChapter = child?.tag as WarpChapter
                if(warpChapter.selected){
                    warpChapter.selected = false
                }
                dragLayoutTodo.dragItemManager.addItem(data)
            }
        })
        dragLayoutDone.setDragAdapter(object : DragAdapter<WarpChapter>() {

            override fun getItemLayoutId(): Int  = R.layout.paccounts_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: WarpChapter?) {
                itemView?.isSelected = true
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView?.findViewById(R.id.close) as View
                closeView.visibility = if (dragState !== DragFlowLayout.DRAG_STATE_IDLE)
                    View.VISIBLE
                else
                    View.INVISIBLE
            }

            override fun getData(itemView: View?): WarpChapter = itemView?.tag as WarpChapter
        })
        dragLayoutDone.setOnDragStateChangeListener { dfl, dragState ->
            finish.text = if(dragState == DRAG_STATE_IDLE){
                "编辑"
            }else{
                "完成"
            }
            finish.isEnabled = dragState != DRAG_STATE_DRAGGING
        }
        finish.setOnClickListener {
            if(dragLayoutDone.dragState == DRAG_STATE_IDLE){
                dragLayoutDone.beginDrag()
                finish.text = "完成"
            }else{
                dragLayoutDone.finishDrag()
                finish.text = "编辑"
            }
        }

        dragLayoutTodo.setDragAdapter(object : DragAdapter<WarpChapter>() {

            override fun getItemLayoutId(): Int  = R.layout.paccounts_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: WarpChapter?) {
                itemView?.isSelected = false
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView?.findViewById(R.id.close) as View
                closeView.visibility = View.INVISIBLE
                itemView.setOnClickListener {
                    if(data != null && !data.selected){
                        data?.selected = true
                        dragLayoutTodo.dragItemManager.removeItem(data)
                        dragLayoutDone.dragItemManager.addItem(data)
                    }
                }
            }

            override fun getData(itemView: View?): WarpChapter = itemView?.tag as WarpChapter
        })

        dragLayoutTodo.setDraggable(false)

        for(item in data){
            val warpChapter = WarpChapter(item)
            tags.add(warpChapter)
            dragLayoutDone.dragItemManager.addItem(warpChapter)
        }
    }

    private fun initData() {
        pageState.visibility = View.VISIBLE
        fetchProjectDisposable = viewModel.getPaccounts()
                .subscribe({
                    if (it.isSuccess) {
                        if (!it.data.isNullOrEmpty()) {
                            pageState.visibility = View.GONE
                            projects = it.data!!

                            initDragData(projects!!)
                            //设置tab
                            articlePager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

                                override fun getItem(position: Int): Fragment {
                                    var fragment: ArticleFragment? = articleFragments[position]
                                    if (fragment == null) {
                                        fragment = ArticleFragment(projects!![position])
                                        articleFragments[position] = fragment
                                    }
                                    return fragment
                                }

                                override fun getCount(): Int = projects!!.size

                                override fun getPageTitle(position: Int): CharSequence? = projects!![position].name
                            }
                            tabLayout.setupWithViewPager(articlePager)
                        } else {
                            pageState.toEmpty("当前页面没有项目", "尝试刷新", Runnable {
                                pageState.toLoading("正在加载")
                                initData()
                            })
                        }
                    } else {
                        pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                            pageState.toLoading("正在加载")
                            initData()
                        })
                    }

                }, {
                    pageState.toEmpty("网络请求失败", "重新刷新", Runnable {
                        pageState.toLoading("正在加载")
                        initData()
                    })
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchProjectDisposable != null && fetchProjectDisposable!!.isDisposed) {
            fetchProjectDisposable?.dispose()
        }
    }
}