package com.effective.android.component.weibo.view.fragment

import android.accounts.Account
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.rxjava.Rx2Schedulers
import com.effective.android.component.weibo.CircleTextProgressbar
import com.effective.android.component.weibo.R
import com.effective.android.component.weibo.videmodel.SplashViewModel
import com.effective.android.component.weibo.databinding.CweiboFragmentSplashLayoutBinding
import com.plugin.component.ComponentManager
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class SplashFragment : BaseVmFragment<SplashViewModel>() {

    private var mDisposable: Disposable? = null
    private val PROGRESS_DEFAULT_WHAT = 0x01
    private lateinit var dataBinding: CweiboFragmentSplashLayoutBinding

    override fun getViewModel(): Class<SplashViewModel> {
        return SplashViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.cweibo_fragment_splash_layout
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = DataBindingUtil.bind<CweiboFragmentSplashLayoutBinding>(view!!)!!
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.loadingProgress.progressType = CircleTextProgressbar.ProgressType.COUNT
        dataBinding.loadingProgress.setProgressLineWidth(5)
        dataBinding.loadingProgress.timeMillis = 3000
        dataBinding.loadingProgress.setProgressColor(ContextCompat.getColor(context!!, R.color.colorTextSecondary))
        dataBinding.loadingProgress.setOutLineColor(Color.WHITE)
        dataBinding.loadingProgress.setInCircleColor(Color.WHITE)
        dataBinding.loadingProgress.setCountdownProgressListener(PROGRESS_DEFAULT_WHAT) { what, progress ->
            if (what == PROGRESS_DEFAULT_WHAT && progress == 100) {
                mDisposable = checkLoginStatus()
            }
        }
        dataBinding.loadingProgress.start()
        dataBinding.loadingProgress.setOnClickListener {
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus(): Disposable {
        return viewModel.checkLoginStatus()
                .compose(Rx2Schedulers.flowableIoToMain())
                .subscribe(object : Consumer<Account>() {
                    @Throws(Exception::class)
                    fun accept(account: Account?) {
                        if (account == null) {
                            ServiceManager.getService(IAccountService::class.java).login(
                                    ComponentManager.getComponent(IDemoComponent::class.java).getMainPath())
                        } else {
                            //go to democomponent main activity
                            ComponentManager.getComponent(IDemoComponent::class.java).gotoMainActivity()
                        }
                        finish()
                    }
                }, object : Consumer<Throwable>() {
                    @Throws(Exception::class)
                    fun accept(throwable: Throwable) {
                        Log.e("", "check login status fail :" + throwable.message)
                        ServiceManager.getService(IAccountService::class.java).login(
                                ComponentManager.getComponent(IDemoComponent::class.java).getMainPath())
                        finish()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null && mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }
}
