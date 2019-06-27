package example.componentweibo.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import example.basiclib.activity.BaseFragment;
import example.basiclib.event.RefreshSkinEvent;
import example.basiclib.net.resource.Resource;
import example.basiclib.util.EventBusUtils;
import example.weibocomponent.R;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.databinding.DemoFragmentHomeLayoutBinding;
import example.weibocomponent.videmodel.HomeViewModel;
import example.weibocomponent.view.adapter.StatusListAdapter;
import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class HomeFragment extends BaseFragment<HomeViewModel, DemoFragmentHomeLayoutBinding> {

    private LinearLayoutManager mLinearLayoutManager;
    public StatusListAdapter statusListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unRegister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshSkinEvent refreshSkinEvent) {
        if (refreshSkinEvent.color != null) {
            dataBinding.swipeLayout.setColorSchemeColors(refreshSkinEvent.color);
        }
    }

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
        int color = SkinCompatManager.getInstance() != null ?
                SkinCompatResources.getColor(getContext(), R.color.colorPrimary) : Color.WHITE;
        dataBinding.swipeLayout.setColorSchemeColors(color);
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
