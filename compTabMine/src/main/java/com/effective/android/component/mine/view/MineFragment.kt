package com.effective.android.component.mine.view

import android.os.Bundle
import android.view.View
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.toast.ToastUtils
import com.effective.android.base.view.dialog.CommonDialog
import com.effective.android.component.mine.R
import com.effective.android.component.mine.vm.MineViewModel
import com.effective.android.service.account.AccountChangeListener
import com.effective.android.service.account.UserInfo
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.mine_fragment_main_layout.*

class MineFragment : BaseVmFragment<MineViewModel>() {

    private var userInfo: UserInfo? = null
    private lateinit var accountDisposable: Disposable
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
        if (!accountDisposable.isDisposed) {
            accountDisposable.dispose()
        }
    }

    private fun initListener() {
        avatar.setOnClickListener {
            if (isLogin()) {
                CommonDialog.Builder(it.context)
                        .content("确认退出登录吗")
                        .left("取消")
                        .right("确定", View.OnClickListener {
                            viewModel.logout()
                        })
                        .build()
                        .show()
            }
        }
        viewModel.addAccountChangeListener(listener)
    }

    private fun initData() {
        checkoutStatus(isLogin())
        accountDisposable = viewModel.getLoginAccount()
                .subscribe({
                    if (it.isValid()) {
                        userInfo = it
                    }
                    checkoutStatus(isLogin())
                }, {
                    checkoutStatus(isLogin())
                })
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