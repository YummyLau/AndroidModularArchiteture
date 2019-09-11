package com.effective.android.component.weibo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.component.weibo.R
import com.effective.android.component.weibo.data.local.db.entity.StatusEntity


import com.effective.android.component.weibo.databinding.DemoFragmentHomeLayoutBinding
import com.effective.android.component.weibo.net.resource.Resource
import com.effective.android.component.weibo.videmodel.HomeViewModel
import com.effective.android.component.weibo.view.adapter.StatusListAdapter


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class HomeFragment(weiboMainFragment: WeiboMainFragment) : BaseVmFragment<HomeViewModel>() {

    private var mLinearLayoutManager: LinearLayoutManager? = null

    lateinit var statusListAdapter: StatusListAdapter

    private var dataBinding: DemoFragmentHomeLayoutBinding? = null


    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.demo_fragment_home_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onEvent(RefreshSkinEvent refreshSkinEvent) {
    //        if (refreshSkinEvent.color != null) {
    //            dataBinding.swipeLayout.setColorSchemeColors(refreshSkinEvent.color);
    //        }
    //    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = DataBindingUtil.bind(contentView!!)
        dataBinding!!.setViewmodel(viewModel)
        initView()
        viewModel.getAllStatus().observe(this, Observer<Resource<List<StatusEntity>>> { listResource ->
            if (listResource!!.data != null) {
                statusListAdapter.addData(listResource.data!!)
            }
            dataBinding!!.swipeLayout.isRefreshing = listResource.loading()
        })
        //        int color = SkinCompatManager.getInstance() != null ?
        //                SkinCompatResources.getColor(getContext(), R.color.colorPrimary) : Color.WHITE;
        //        dataBinding.swipeLayout.setColorSchemeColors(color);
        return dataBinding!!.root
    }

    private fun initView() {
        mLinearLayoutManager = LinearLayoutManager(context)
        statusListAdapter = StatusListAdapter(R.layout.demo_item_status_layout, null)
        statusListAdapter.openLoadAnimation(StatusListAdapter.SLIDEIN_LEFT)
        dataBinding!!.statusList.layoutManager = mLinearLayoutManager
        dataBinding!!.swipeLayout.setOnRefreshListener { viewModel.refresh() }
        dataBinding!!.statusList.adapter = statusListAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}
