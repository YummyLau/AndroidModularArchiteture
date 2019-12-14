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
import com.effective.android.base.view.tagdrag.IViewObserver


class PaccountsFragment: BaseVmFragment<PaccountsViewModel>() {

    private var fetchProjectDisposable: Disposable? = null
    val articleFragments: HashMap<Int, ArticleFragment> = HashMap()
    var projects: List<Chapter>? = null

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
        dragLayout.setOnItemClickListener(object : ClickToDeleteItemListenerImpl(R.id.close) {

            override fun performClick(dragFlowLayout: DragFlowLayout?, child: View?, event: MotionEvent?, dragState: Int): Boolean {
                return super.performClick(dragFlowLayout, child, event, dragState)
            }

            override fun onDeleteSuccess(dfl: DragFlowLayout?, child: View?, data: Any?) {
                super.onDeleteSuccess(dfl, child, data)
            }
        })
    }

    private fun initDragData(data : List<Chapter>){
        //设置筛选框
        dragLayout.setDragAdapter(object : DragAdapter<Chapter>() {

            override fun getItemLayoutId(): Int  = R.layout.paccounts_item_drag_layout

            override fun onBindData(itemView: View?, dragState: Int, data: Chapter?) {
                itemView?.tag = data
                val tv = itemView?.findViewById(R.id.content) as TextView
                tv.text = data?.name
                //iv_close是关闭按钮。只有再非拖拽空闲的情况下才显示
                val closeView = itemView?.findViewById(R.id.close) as View
                closeView.visibility = if (dragState !== DragFlowLayout.DRAG_STATE_IDLE)
                    View.VISIBLE
                else
                    View.INVISIBLE
            }

            override fun getData(itemView: View?): Chapter = itemView?.tag as Chapter
        })

        //预存指定个数的Item. 这些Item view会反复使用，避免重复创建, 默认10个
        dragLayout.prepareItemsByCount(10)
        //设置拖拽状态监听器
        dragLayout.setOnDragStateChangeListener { dfl, dragState ->
            Log.i("paaa", "on drag state change : dragState = $dragState")
        }
        //添加view观察者
        dragLayout.addViewObserver(object : IViewObserver {
            override fun onAddView(child: View, index: Int) {
                 Log.i("paaa", "onAddView index = $index");
            }

            override fun onRemoveView(child: View, index: Int) {
                Log.i("paaa", "onRemoveView  index = $index");
            }
        })

        for(item in data){
            dragLayout.dragItemManager.addItem(item)
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