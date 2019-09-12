package com.effective.android.component.weibo.view.fragment


import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer


import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.util.FragmentUtils
import com.effective.android.component.weibo.R
import com.effective.android.component.weibo.WeiboComponent
import com.effective.android.component.weibo.data.local.db.entity.UserEntity

import com.effective.android.component.weibo.databinding.DemoActivityMainLayoutBinding
import com.effective.android.component.weibo.net.resource.Resource
import com.effective.android.component.weibo.videmodel.MainViewModel
import com.effective.android.service.account.Account
import com.effective.android.service.account.AccountResult
import kotlinx.android.synthetic.main.demo_activity_main_layout.*


/**
 * 微博模块主界面
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/1/24.
 */

class WeiboMainFragment : BaseVmFragment<MainViewModel>() {

    private lateinit var mHomeFragment: FeedFragment
    private lateinit var mSkinFragment: SkinFragment
    private lateinit var mSplashFragment: SplashFragment

    private lateinit var dataBinding: DemoActivityMainLayoutBinding

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.demo_activity_main_layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding = DataBindingUtil.bind(rootView!!)!!
        initData()
    }

    private fun initData() {
        mHomeFragment = FeedFragment(this)
        mSkinFragment = SkinFragment(this)
        mSplashFragment = SplashFragment(this)
        FragmentUtils.replace(childFragmentManager, R.id.splash_container, mSplashFragment)
    }

    fun finishSplashView() {
        toMainView()
    }

    private fun toMainView() {
        splash_container.visibility = View.GONE
        content_container.visibility = View.VISIBLE
        if (WeiboComponent.accountSdk.isLogin()) {
            initMainView(true)
        } else {
            initMainView(false)
            WeiboComponent.accountSdk.login(context!!, object : AccountResult {
                override fun onResult(account: Account?) {
                    if (account != null) {
                        initMainView(true)
                    }
                }
            })
        }
    }

    private fun initMainView(login: Boolean) {
        FragmentUtils.replace(childFragmentManager, R.id.fragment_container, mHomeFragment)
        title.text = "首页"
        if (login) {

        } else {
            avatar.setImageResource(R.drawable.cweibo_ic_default_avatar)
        }
    }


    private fun toLoadData() {
        viewModel.getUser().observe(this, Observer<Resource<UserEntity>> { userEntityResource ->
            if (userEntityResource!!.data != null) {
//                (view.findViewById<View>(R.id.nick) as TextView).text = userEntityResource.data!!.name
//                (view.findViewById<View>(R.id.status_tip) as TextView).text = String.format(context!!.getString(R.string.demo_weibo_count_tip), userEntityResource.data.statusesCount)
//                (view.findViewById<View>(R.id.follow_tip) as TextView).text = String.format(context!!.getString(R.string.demo_follows_count_tip), userEntityResource.data.friendsCount)
//                (view.findViewById<View>(R.id.fans_tip) as TextView).text = String.format(context!!.getString(R.string.demo_fans_count_tip), userEntityResource.data.followersCount)
//                                    Glide.with(MainActivity.this)
//                                            .load(userEntityResource.data.profileImageUrl)
//                                            .into((ImageView) view.findViewById(R.id.avatar));
            }
        })
//        accountService = ServiceManager.getService(IAccountService.class);
    }


//    override fun onActivityReenter(resultCode: Int, data: Intent) {
//        super.onActivityReenter(resultCode, data)
//        mHomeFragment!!.statusListAdapter.setExitPostion(ImageViewer.getExitPostion(resultCode, data, 0))
//    }
}
