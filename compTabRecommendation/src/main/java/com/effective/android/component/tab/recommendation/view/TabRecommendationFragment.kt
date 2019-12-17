package com.effective.android.component.tab.recommendation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.effective.android.base.activity.swipeback.SwipeBackLayout
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.view.dragable.card.CardItemTouchHelperCallback
import com.effective.android.base.view.dragable.card.CardLayoutManager
import com.effective.android.base.view.dragable.card.OnSwipeListener
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R
import com.effective.android.component.tab.recommendation.adapter.RecommendAdapter
import com.effective.android.component.tab.recommendation.vm.TabRecommendationVm
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.tabr_fragment_recommendation_layout.*
import kotlin.math.abs

class TabRecommendationFragment : BaseVmFragment<TabRecommendationVm>() {

    var pageNum: Int = 0
    var fetchDataDisposable: Disposable? = null
    lateinit var adapter: RecommendAdapter
    lateinit var data: MutableList<Article>

    override fun getViewModel(): Class<TabRecommendationVm> = TabRecommendationVm::class.java

    override fun getLayoutRes(): Int = R.layout.tabr_fragment_recommendation_layout


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        data = mutableListOf()
        list.itemAnimator = DefaultItemAnimator()
        adapter = RecommendAdapter(context!!, data)
        list.adapter = adapter
        val cardItemTouchHelperCallback = CardItemTouchHelperCallback<Article>(adapter, data)
        cardItemTouchHelperCallback.setOnSwipedListener(object : OnSwipeListener<Article> {
            override fun onSwiping(viewHolder: ViewHolder?, ratio: Float, direction: Int) {
                viewHolder?.itemView?.alpha = 1 - abs(ratio) * 0.2f
                viewHolder?.itemView?.findViewById<View>(R.id.root)?.alpha = 1 - abs(ratio) * 0.2f
            }

            override fun onSwiped(viewHolder: ViewHolder?, t: Article?, direction: Int) {
                viewHolder?.itemView?.alpha = 1f
                viewHolder?.itemView?.findViewById<View>(R.id.root)?.alpha = 1f
            }

            override fun onSwipedClear() {

            }
        })
        val touchHelper = ItemTouchHelper(cardItemTouchHelperCallback)
        val layoutManager = CardLayoutManager(list, touchHelper)
        list.layoutManager = layoutManager
        touchHelper.attachToRecyclerView(list)
        fetchData(true)
    }

    private fun fetchData(boolean: Boolean) {
        pageNum = if (boolean) 0 else pageNum
        fetchDataDisposable = viewModel.getRecommendList(pageNum)
                .subscribe({
                    if (it.isSuccess) {
                        adapter.setData(it.data!!.data)
                        pageNum++
                    }
//                    if (boolean) {
//                        listContainer.finishRefresh(2000, it.isSuccess, false)
//                    } else {
//                        listContainer.finishLoadMore(2000, it.isSuccess, false)
//                    }
                }, {
                    //                    if (boolean) {
//                        listContainer.finishRefresh(2000, false, false)
//                    } else {
//                        listContainer.finishLoadMore(2000, false, false)
//                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fetchDataDisposable != null && fetchDataDisposable!!.isDisposed) {
            fetchDataDisposable?.dispose()
        }
    }
}