package example.weibocomponent.view;


import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import example.basiclib.activity.BaseActivity;
import example.basiclib.net.resource.Resource;
import example.basiclib.widget.imageviewer.ImageViewer;
import example.componentlib.service.ServiceManager;
import example.componentlib.service.account.IAccountService;
import example.componentlib.service.skin.ISkinService;
import example.weibocomponent.Constants;
import example.weibocomponent.R;
import example.weibocomponent.data.local.db.entity.UserEntity;
import example.weibocomponent.databinding.DemoActivityMainLayoutBinding;
import example.weibocomponent.videmodel.MainViewModel;

import static it.sephiroth.android.library.imagezoom.ImageViewTouchBase.LOG_TAG;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/1/24.
 */

@Route(path = Constants.ROUTER_MAIN)
public class MainActivity extends BaseActivity<MainViewModel, DemoActivityMainLayoutBinding> {

    private List<Fragment> mFragments;
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private SkinFragment mSkinFragment;

    private IAccountService accountService;
    private ISkinService skinService;
    private boolean hasChangeSkin = false;

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.demo_activity_main_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mFragments = new ArrayList<>();
        mHomeFragment = new HomeFragment();
        mSkinFragment = new SkinFragment();
        mFragments.add(mHomeFragment);
        mFragments.add(mSkinFragment);
        mFragmentManager = super.getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragments.get(0), null).commit();
        initView();
        viewModel.getUser().observe(this, new Observer<Resource<UserEntity>>() {
            @Override
            public void onChanged(@Nullable Resource<UserEntity> userEntityResource) {
                if (userEntityResource.data != null) {
                    View view = dataBinding.navigationLayout.getHeaderView(0);
                    ((TextView) view.findViewById(R.id.nick)).setText(userEntityResource.data.name);
                    ((TextView) view.findViewById(R.id.status_tip)).setText(String.format(MainActivity.this.getString(R.string.demo_weibo_count_tip), userEntityResource.data.statusesCount));
                    ((TextView) view.findViewById(R.id.follow_tip)).setText(String.format(MainActivity.this.getString(R.string.demo_follows_count_tip), userEntityResource.data.friendsCount));
                    ((TextView) view.findViewById(R.id.fans_tip)).setText(String.format(MainActivity.this.getString(R.string.demo_fans_count_tip), userEntityResource.data.followersCount));
                    Glide.with(MainActivity.this)
                            .load(userEntityResource.data.profileImageUrl)
                            .into((ImageView) view.findViewById(R.id.avatar));
                }
            }
        });
        viewModel.initInfo();
        accountService = ServiceManager.getService(IAccountService.class);
        skinService = ServiceManager.getService(ISkinService.class);
    }


    private void initView() {
        setSupportActionBar(dataBinding.toolbar);
        dataBinding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextIcon));
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dataBinding.drawerLayout,
                dataBinding.toolbar,
                R.string.demo_navigation_drawer_open,
                R.string.demo_navigation_drawer_close);
        dataBinding.drawerLayout.addDrawerListener(toggle);
        dataBinding.navigationLayout.setItemBackground(null);
        dataBinding.navigationLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_main) {
                    mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragments.get(0), null).commit();
                } else if (item.getItemId() == R.id.nav_skin_peeling) {
                    mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragments.get(1), null).commit();
                } else if (item.getItemId() == R.id.nav_logout) {
                    if (accountService != null) {
                        accountService.logout(true, Constants.ROUTER_MAIN);
                        finish();
                        Toast.makeText(MainActivity.this, R.string.demo_menu_logout, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(LOG_TAG, "IAccountService was not be resigted!");
                    }
                }
                dataBinding.toolbar.setTitle(item.getTitle());
                dataBinding.drawerLayout.closeDrawers();
                return true;
            }
        });
        toggle.syncState();
        dataBinding.navigationLayout.setCheckedItem(R.id.nav_main);
        dataBinding.toolbar.setTitle(R.string.demo_menu_home);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            dataBinding.drawerLayout.openDrawer(GravityCompat.START);//打开侧滑菜单
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean supportHandlerStatusBar() {
        return false;
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, dataBinding.drawerLayout, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mHomeFragment.statusListAdapter.setExitPostion(ImageViewer.getExitPostion(resultCode, data, 0));
    }
}
