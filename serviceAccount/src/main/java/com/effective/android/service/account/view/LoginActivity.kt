package com.effective.android.service.account.view

import android.os.Bundle
import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.service.account.R
import com.effective.android.service.account.vm.LoginViewModel
import kotlinx.android.synthetic.main.account_activity_login_layout.*

/**
 * login activity
 * Email yummyl.lau@gmail.com
 * 登陆页面
 * Created by yummylau on 2018/01/25.
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    var isSelectLogin: Boolean = true


    override fun getViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.account_activity_login_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(isSelectLogin)
        initListener()
    }

    private fun initListener() {
        actionTip.setOnClickListener {
            isSelectLogin = !isSelectLogin
            initView(isSelectLogin)
        }
        action.setOnClickListener {
            var userName = userName.editableText.toString()
            var password = password.editableText.toString()

        }
    }

    private fun initView(isSelectLogin: Boolean) {
        pageTitle.text = getString(if (isSelectLogin) R.string.saccount_login else R.string.saccount_register)
        action.text = getString(if (isSelectLogin) R.string.saccount_login else R.string.saccount_register_and_login)
        actionTip.text = getString(if (isSelectLogin) R.string.saccount_login_tip else R.string.saccount_register_tip)
    }

}
