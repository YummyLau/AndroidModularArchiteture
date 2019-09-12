package com.effective.android.service.account.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.base.util.FontUtils
import com.effective.android.service.account.Account
import com.effective.android.service.account.AccountComponent
import com.effective.android.service.account.R
import com.effective.android.service.account.databinding.AccountActivityLoginLayoutBinding
import com.sina.weibo.sdk.auth.sso.SsoHandler

import com.effective.android.service.account.vm.LoginViewModel

/**
 * login activity
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    private var mSsoHandler: SsoHandler? = null
    private lateinit var dataBinding: AccountActivityLoginLayoutBinding

    override fun getViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.account_activity_login_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.bind<AccountActivityLoginLayoutBinding>(rootView)!!
        mSsoHandler = SsoHandler(this)
//        dataBinding.accountResult = object : AccountResult {
//            override fun onResult(account: Account?) {
//                AccountComponent.syncLoginResult(account)
//                finish()
//            }
//        }
        initView()
    }

    private fun initView() {
        FontUtils.replaceFontFromAsset(dataBinding.title, "fonts/DIN-Condensed-Bold-2.ttf")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //如果发起sso授权回调
        if (mSsoHandler != null) {
            mSsoHandler!!.authorizeCallBack(requestCode, resultCode, data)
        }
    }
}
