package com.effective.android.component.weibo.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer


import java.util.ArrayList

import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.component.weibo.R
import com.effective.android.component.weibo.data.local.db.entity.UserEntity

import com.effective.android.component.weibo.databinding.DemoActivityMainLayoutBinding
import com.effective.android.component.weibo.imageviewer.ImageViewer
import com.effective.android.component.weibo.net.resource.Resource
import com.effective.android.component.weibo.videmodel.MainViewModel
import com.effective.android.component.weibo.view.fragment.HomeFragment
import com.effective.android.component.weibo.view.fragment.SkinFragment


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/1/24.
 */

class MainActivity : BaseVmActivity<MainViewModel>() {

    private var mFragments: MutableList<Fragment>? = null
    private var mFragmentManager: FragmentManager? = null
    private var mHomeFragment: HomeFragment? = null
    private var mSkinFragment: SkinFragment? = null

    private var dataBinding: DemoActivityMainLayoutBinding? = null
    //    private IAccountService accountService;

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }


    override fun getLayoutRes(): Int {
        return R.layout.demo_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.bind(contentView())
        mFragments = ArrayList()
        mHomeFragment = HomeFragment()
        mSkinFragment = SkinFragment()
        mFragments!!.add(mHomeFragment!!)
        mFragments!!.add(mSkinFragment!!)
        mFragmentManager = super.getSupportFragmentManager()
        initView()
        viewModel.getUser().observe(this, Observer<Resource<UserEntity>> { userEntityResource ->
            if (userEntityResource!!.data != null) {
                val view = dataBinding!!.navigationLayout.getHeaderView(0)
                (view.findViewById<View>(R.id.nick) as TextView).text = userEntityResource.data!!.name
                (view.findViewById<View>(R.id.status_tip) as TextView).text = String.format(this@MainActivity.getString(R.string.demo_weibo_count_tip), userEntityResource.data.statusesCount)
                (view.findViewById<View>(R.id.follow_tip) as TextView).text = String.format(this@MainActivity.getString(R.string.demo_follows_count_tip), userEntityResource.data.friendsCount)
                (view.findViewById<View>(R.id.fans_tip) as TextView).text = String.format(this@MainActivity.getString(R.string.demo_fans_count_tip), userEntityResource.data.followersCount)
                //                    Glide.with(MainActivity.this)
                //                            .load(userEntityResource.data.profileImageUrl)
                //                            .into((ImageView) view.findViewById(R.id.avatar));
            }
        })
        viewModel.initInfo()
        //        accountService = ServiceManager.getService(IAccountService.class);
    }


    private fun initView() {
        setSupportActionBar(dataBinding!!.toolbar)
        supportActionBar!!.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mFragmentManager!!.beginTransaction().add(
                R.id.content_frame, mFragments!![0], getString(R.string.demo_menu_home)).show(mFragments!![0]).commit()
        supportActionBar!!.setTitle(R.string.demo_menu_home)
        dataBinding!!.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextIcon))

        val toggle = ActionBarDrawerToggle(
                this,
                dataBinding!!.drawerLayout,
                dataBinding!!.toolbar,
                R.string.demo_navigation_drawer_open,
                R.string.demo_navigation_drawer_close)
        dataBinding!!.drawerLayout.addDrawerListener(toggle)
        dataBinding!!.navigationLayout.itemBackground = null
        dataBinding!!.navigationLayout.setNavigationItemSelectedListener { item ->
            if (item.itemId != R.id.nav_logout) {
                var fragment = mFragmentManager!!.findFragmentByTag(item.title.toString())
                val fragments = mFragmentManager!!.fragments
                val fragmentTransaction = mFragmentManager!!.beginTransaction()
                if (fragment == null) {
                    if (item.itemId == R.id.nav_main) {
                        fragment = mFragments!![0]
                    } else {
                        fragment = mFragments!![1]
                    }
                    fragmentTransaction.add(R.id.content_frame, fragment, item.title.toString()).show(fragment)
                    fragments.add(fragment)
                } else {
                    fragmentTransaction.show(fragment)
                }
                for (i in fragments.indices) {
                    if (fragments[i] !== fragment) {
                        fragmentTransaction.hide(fragments[i])
                    }
                }
                fragmentTransaction.commit()
            }

            if (item.itemId == R.id.nav_logout) {
                //                    if (accountService != null) {
                //                        accountService.logout(true, Constants.ROUTER_MAIN);
                //                        finish();
                //                        Toast.makeText(MainActivity.this, R.string.demo_menu_logout, Toast.LENGTH_SHORT).show();
                //                    } else {
                //                        Log.w("", "IAccountService was not be resigted!");
                //                    }
            }
            dataBinding!!.toolbar.title = item.title
            dataBinding!!.drawerLayout.closeDrawers()
            true
        }
        toggle.syncState()
        dataBinding!!.navigationLayout.setCheckedItem(R.id.nav_main)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            dataBinding!!.drawerLayout.openDrawer(GravityCompat.START)//打开侧滑菜单
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        mHomeFragment!!.statusListAdapter.setExitPostion(ImageViewer.getExitPostion(resultCode, data, 0))
    }
}
