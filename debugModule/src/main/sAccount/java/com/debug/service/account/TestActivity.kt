package com.debug.service.account

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.debug.R

import com.effective.android.base.activity.BaseActivity
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.base.toast.ToastUtils
import com.effective.android.base.util.GsonUtils
import com.effective.android.service.account.AccountChangeListener
import com.effective.android.service.account.AccountSdk
import com.effective.android.service.account.UserInfo
import com.plugin.component.ComponentManager
import com.plugin.component.SdkManager


class TestActivity : BaseActivity(), AccountChangeListener {

    private var userInfo: UserInfo? = null
    private var accountSdk: AccountSdk? = null

    override fun getLayoutRes(): Int {
        return R.layout.account_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity_main_layout)
        StatusbarHelper.translucentStatusBar(this)
        init()
    }

    private fun init() {
        ComponentManager.init(application)
        accountSdk = SdkManager.getSdk(AccountSdk::class.java)
        userInfo = accountSdk!!.getAccount()
        accountSdk!!.addAccountChangeListener(this)
        checkoutStatus(userInfo != null, userInfo)
        findViewById<View>(R.id.login).setOnClickListener { accountSdk!!.login(this@TestActivity) }
        findViewById<View>(R.id.logout).setOnClickListener { accountSdk!!.logout() }
    }

    override fun onAccountChange(userInfo: UserInfo?, login: Boolean, success: Boolean, message: String?) {
        runOnUiThread {
            if (success) {
                checkoutStatus(login, userInfo)
            }
            ToastUtils.show(this@TestActivity, message!!)
        }
    }

    private fun checkoutStatus(login: Boolean, userInfo: UserInfo?) {
        findViewById<View>(R.id.login).isEnabled = !login
        findViewById<View>(R.id.logout).isEnabled = login
        (findViewById<View>(R.id.info) as TextView).text = if (login) GsonUtils.getJsonString(userInfo!!) else ""
    }

    override fun onDestroy() {
        super.onDestroy()
        if (accountSdk != null) {
            accountSdk!!.removeAccountChangeListener(this)
        }
    }
}
