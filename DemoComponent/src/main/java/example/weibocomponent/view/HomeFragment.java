package example.weibocomponent.view;

import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import example.basiclib.activity.BaseFragment;
import example.basiclib.net.resource.Resource;
import example.weibocomponent.R;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.databinding.DemoFragmentHomeLayoutBinding;
import example.weibocomponent.videmodel.HomeViewModel;
import example.weibocomponent.view.adapter.StatusListAdapter;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/1/24.
 */

public class HomeFragment extends BaseFragment<HomeViewModel, DemoFragmentHomeLayoutBinding> {

    private LinearLayoutManager mLinearLayoutManager;
    public StatusListAdapter statusListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding.setViewmodel(viewModel);
        initView();
        viewModel.getAllStatus().observe(this, new Observer<Resource<List<StatusEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<StatusEntity>> listResource) {
                if (listResource.data != null) {
                    statusListAdapter.addData(listResource.data);
                }
                dataBinding.swipeLayout.setRefreshing(listResource.loading());
            }
        });
        dataBinding.swipeLayout.setColorSchemeColors(Color.WHITE);
        return dataBinding.getRoot();
    }

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.demo_fragment_home_layout;
    }

    private void initView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        statusListAdapter = new StatusListAdapter(R.layout.demo_item_status_layout, null);
        statusListAdapter.openLoadAnimation(StatusListAdapter.SLIDEIN_LEFT);
        dataBinding.statusList.setLayoutManager(mLinearLayoutManager);
        dataBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
        dataBinding.statusList.setAdapter(statusListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }
}
