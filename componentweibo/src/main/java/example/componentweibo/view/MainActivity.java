package example.componentweibo.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import example.basiclib.activity.BaseActivity;
import example.basiclib.net.resource.Resource;
import example.basiclib.widget.imageviewer.ImageViewer;
import example.componentlib.service.ServiceManager;
import example.componentlib.service.account.IAccountService;
import example.weibocomponent.Constants;
import example.weibocomponent.R;
import example.weibocomponent.data.local.db.entity.UserEntity;
import example.weibocomponent.databinding.DemoActivityMainLayoutBinding;
import example.weibocomponent.videmodel.MainViewModel;


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
    }


    private void initView() {
        setSupportActionBar(dataBinding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragmentManager.beginTransaction().add(
                R.id.content_frame, mFragments.get(0), getString(R.string.demo_menu_home)).show(mFragments.get(0)).commit();
        getSupportActionBar().setTitle(R.string.demo_menu_home);
        dataBinding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextIcon));

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

                if (item.getItemId() != R.id.nav_logout) {
                    Fragment fragment = mFragmentManager.findFragmentByTag(item.getTitle().toString());
                    List<Fragment> fragments = mFragmentManager.getFragments();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    if (fragment == null) {
                        if (item.getItemId() == R.id.nav_main) {
                            fragment = mFragments.get(0);
                        } else {
                            fragment = mFragments.get(1);
                        }
                        fragmentTransaction.add(R.id.content_frame, fragment, item.getTitle().toString()).show(fragment);
                        fragments.add(fragment);
                    } else {
                        fragmentTransaction.show(fragment);
                    }
                    for (int i = 0; i < fragments.size(); i++) {
                        if (fragments.get(i) != fragment) {
                            fragmentTransaction.hide(fragments.get(i));
                        }
                    }
                    fragmentTransaction.commit();
                }

                if (item.getItemId() == R.id.nav_logout) {
                    if (accountService != null) {
                        accountService.logout(true, Constants.ROUTER_MAIN);
                        finish();
                        Toast.makeText(MainActivity.this, R.string.demo_menu_logout, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("", "IAccountService was not be resigted!");
                    }
                }
                dataBinding.toolbar.setTitle(item.getTitle());
                dataBinding.drawerLayout.closeDrawers();
                return true;
            }
        });
        toggle.syncState();
        dataBinding.navigationLayout.setCheckedItem(R.id.nav_main);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            dataBinding.drawerLayout.openDrawer(GravityCompat.START);//打开侧滑菜单
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mHomeFragment.statusListAdapter.setExitPostion(ImageViewer.getExitPostion(resultCode, data, 0));
    }
}
