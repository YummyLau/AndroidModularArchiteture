package com.effective.android.example.view

import android.graphics.Typeface
import android.os.Bundle
import com.effective.android.base.fragment.BaseVmFragment
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.util.SharePreferencesUtil
import com.effective.android.example.R
import com.effective.android.example.vm.SplashVm
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.app_fragment_splash.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 闪屏模块
 * created by yummylau on 2019/12/13
 */
class SplashFragment : BaseVmFragment<SplashVm>() {

    private var disposable: Disposable? = null
    private val splashKey = "splash_key"

    override fun getViewModel(): Class<SplashVm> = SplashVm::class.java

    override fun getLayoutRes(): Int = R.layout.app_fragment_splash

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        appTip.typeface = Typeface.createFromAsset(context?.assets, ResourceUtils.getString(activity!!, R.string.app_splash_text_font))
        splashSkip.setOnClickListener {
            toHome()
        }
        loadDifferentSplashImage()
        disposable = Flowable.timer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    toHome()
                }, {
                    toHome()
                })
    }

    private fun toHome() {
        disposable?.dispose()
        (activity as MainActivity).toHome()
    }

    private fun loadDifferentSplashImage() {
        val index = SharePreferencesUtil.getInt(context!!, splashKey, 1)
        splashImage.setImageDrawable(ResourceUtils.getDrawable(activity!!, String.format(activity!!.getString(R.string.app_splash_image_prefix), index.toString())))
        SharePreferencesUtil.putInt(context!!, splashKey, if (index == 9) 1 else index + 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }

}