package com.effective.android.component.mine.view

import android.os.Bundle
import android.view.View
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.toast.ToastUtils
import com.effective.android.component.mine.R
import com.effective.android.component.mine.vm.MineViewModel
import com.effective.android.service.account.AccountChangeListener
import com.effective.android.service.account.UserInfo
import kotlinx.android.synthetic.main.mine_fragment_main_layout.*

class MineFragment : BaseVmFragment<MineViewModel>() {

    private var userInfo: UserInfo? = null

    private val listener = object : AccountChangeListener {

        override fun onAccountChange(userInfo: UserInfo?, login: Boolean, success: Boolean, message: String?) {
            if (success) {
                this@MineFragment.userInfo = if (login) userInfo else null
                checkoutStatus(isLogin())
            } else {
                ToastUtils.show(context!!, message ?: "操作失败")
            }
        }
    }

    override fun getViewModel(): Class<MineViewModel> = MineViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.mine_fragment_main_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeAccountChangeListener(listener)
    }

    private fun initListener() {
        avatar.setOnClickListener {
            if (isLogin()) {
                viewModel.logout()
            }
        }
        viewModel.addAccountChangeListener(listener)
    }

    private fun initData() {
        userInfo = viewModel.getAccount()
        checkoutStatus(isLogin())
    }

    private fun checkoutStatus(hasLogin: Boolean) {
        avatar.isSelected = hasLogin
        avatar_bg.isSelected = hasLogin
        if (hasLogin) {
            login.visibility = View.GONE
            avatar.setImageResource(R.drawable.mine_ic_login)
            nick.visibility = View.VISIBLE
            nick.text = userInfo!!.nickname
        } else {
            login.visibility = View.VISIBLE
            avatar.setImageResource(R.drawable.mine_ic_logout)
            nick.visibility = View.GONE
            login.setOnClickListener {
                viewModel.login(context!!)
            }
        }
    }

    private fun isLogin() = userInfo != null
}