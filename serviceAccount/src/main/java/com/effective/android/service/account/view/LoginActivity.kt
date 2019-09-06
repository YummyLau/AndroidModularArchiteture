package com.effective.android.service.account.view

import android.content.Intent
import android.os.Bundle

import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.base.util.FontUtils
import com.effective.android.service.account.R
import com.effective.android.service.account.databinding.AccountActivityLoginLayoutBinding
import com.sina.weibo.sdk.auth.sso.SsoHandler

import com.effective.android.service.account.Constants
import com.effective.android.service.account.vm.LoginViewModel

/**
 * login activity
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    private var returnPath: String? = null
    private var mSsoHandler: SsoHandler? = null
    private val dataBinding: AccountActivityLoginLayoutBinding? = null

    override fun getViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.account_activity_login_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        returnPath = intent.getStringExtra(Constants.RETURN_ACTIVITY_PATH)
        mSsoHandler = SsoHandler(this)
        dataBinding!!.viewmodel = viewModel
        dataBinding.ssohandler = mSsoHandler
        initView()
    }

    private fun initView() {
        FontUtils.replaceFontFromAsset(dataBinding!!.title, "fonts/DIN-Condensed-Bold-2.ttf")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //如果发起sso授权回调
        if (mSsoHandler != null) {
            mSsoHandler!!.authorizeCallBack(requestCode, resultCode, data)
        }
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onEvent(AccountEvent event) {
    //        if (event.type == AccountEvent.LOGIN_TYPE) {
    //            if (!TextUtils.isEmpty(returnPath)) {
    //                ARouter.getInstance().build(returnPath).navigation();
    //            }
    //            finish();
    //        }
    //    }
}
