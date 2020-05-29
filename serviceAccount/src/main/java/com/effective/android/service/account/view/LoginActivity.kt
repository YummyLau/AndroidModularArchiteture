package com.effective.android.service.account.view

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.effective.android.base.activity.BaseVmActivity
import com.effective.android.base.systemui.QMUIStatusBarHelper
import com.effective.android.base.systemui.StatusbarHelper
import com.effective.android.base.toast.ToastUtils.show
import com.effective.android.base.util.ResourceUtils
import com.effective.android.service.account.R
import com.effective.android.service.account.Sdks
import com.effective.android.service.account.util.EditTextUtil
import com.effective.android.service.account.vm.LoginViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.account_activity_login_layout.*
import kotlinx.android.synthetic.main.account_activity_login_layout.view.*

/**
 * logout activity
 * Email yummyl.lau@gmail.com
 * 登陆页面
 * Created by yummylau on 2018/01/25.
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    private var isSelectLogin: Boolean = true
    private var loginDisposable: Disposable? = null
    private var registerDisposable: Disposable? = null
    private lateinit var pop: LoginHistoryPopupWindow


    override fun getViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.account_activity_login_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusbarHelper.setStatusBarColor(this, ResourceUtils.getColor(this, R.color.blockBackground))
        if(Sdks.serviceSkin.isLoadingDefaultSkin()){
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }else{
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
        initView(isSelectLogin)
        initListener()
    }

    private fun checkoutUserName(s: Editable?, hasFocus: Boolean) {
        if (hasFocus) {
            var hasText = s?.isNotEmpty()
            if (hasText != null && hasText) {
                userNameClear.visibility = View.VISIBLE
                userNameList.visibility = View.GONE
            } else {
                userNameClear.visibility = View.GONE
                userNameList.visibility = if (viewModel.getLoginHistory().isNullOrEmpty() || !isSelectLogin) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        } else {
            userNameClear.visibility = View.GONE
            userNameList.visibility = if (viewModel.getLoginHistory().isNullOrEmpty() || !isSelectLogin) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    private fun checkoutPassword(s: Editable?, hasFocus: Boolean) {
        if (hasFocus) {
            var hasText = s?.isNotEmpty()
            passwordClear.visibility = if (hasText != null && hasText) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } else {
            passwordClear.visibility = View.GONE
        }
    }

    private fun initListener() {
        actionTip.setOnClickListener {
            isSelectLogin = !isSelectLogin
            initView(isSelectLogin)
        }
        userName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                checkoutUserName(s, true)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        userName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> checkoutUserName(userName.text, hasFocus) }


        password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                checkoutPassword(s, true)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        password.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> checkoutPassword(userName.text, hasFocus) }


        userNameClear.setOnClickListener {
            userName.setText("")
        }

        passwordClear.setOnClickListener {
            password.setText("")
        }

        userNameList.setOnClickListener {
            pop.show(userName, viewModel.getLoginHistory(), object : LoginHistoryPopupWindow.Callback {

                override fun onLoginInfoSelected(name: String, password: String) {
                    userName.setText(name)
                    this@LoginActivity.rootView.password.setText(password)
                }
            })
        }
        action.setOnClickListener {
            var userName = userName.editableText.toString()
            var password = password.editableText.toString()
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                show(this, R.string.account_input_invalid)
                return@setOnClickListener
            }
            if (isSelectLogin) {
                loginDisposable = viewModel.login(userName, password)
                        .subscribe({
                            if (it.isValid()) {
                                finish()
                            }
                        }, {
                            it.message
                        })
            } else registerDisposable = viewModel.register(userName, password)
                    .subscribe({
                        if (it.isValid()) {
                            finish()
                        }
                    }) {
                        it.message
                    }
        }
        titleLayout.leftAction.setOnClickListener {
            finish()
        }
    }

    private fun initView(isSelectLogin: Boolean) {
        pop = LoginHistoryPopupWindow(context = this@LoginActivity)
        titleLayout.setTitle(getString(if (isSelectLogin) R.string.account_login else R.string.account_register))
        titleLayout.leftAction.setOnClickListener {
            finish()
        }
        action.text = getString(if (isSelectLogin) R.string.account_login else R.string.account_register_and_login)
        actionTip.text = getString(if (isSelectLogin) R.string.account_login_tip else R.string.account_register_tip)
        checkoutUserName(userName.text, userName.hasFocus())
        checkoutPassword(password.text, password.hasFocus())
    }

}
