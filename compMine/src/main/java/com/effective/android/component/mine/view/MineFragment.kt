package com.effective.android.component.mine.view

import android.os.Bundle
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
            } else {
                viewModel.login(context!!)
            }
        }
        viewModel.addAccountChangeListener(listener)
    }

    private fun initData() {
        userInfo = viewModel.getAccount()
        checkoutStatus(isLogin())
    }

    private fun checkoutStatus(login: Boolean) {
        if (login) {
            viewModel.loadAvatar(avatar, userInfo!!.icon)
        } else {
            viewModel.loadDefaultAvatar(avatar)
        }
    }

    private fun isLogin() = userInfo != null
}