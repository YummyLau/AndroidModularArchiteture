package example.basiclib.activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import example.basiclib.R;
import example.basiclib.event.RefreshSkinEvent;
import example.basiclib.util.EventBusUtils;
import example.basiclib.util.statusbar.StatusbarHelper;
import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 * {@link dagger.android.support.DaggerAppCompatActivity}
 */

public abstract class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public DB dataBinding;

    private @ColorInt
    int statusBarColor;

    public VM viewModel;

    public abstract Class<VM> getViewModel();

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if (SkinCompatManager.getInstance() == null) {
            statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        } else {
            statusBarColor = SkinCompatResources.getColor(this, R.color.colorPrimary);
        }
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel());
        EventBusUtils.register(this);
        if (supportHandleStatusBar() && !QMUIStatusBarHelper.isFullScreen(this)) {
            handleStatusBar(getStatusBarColor());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unRegister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshSkinEvent refreshSkinEvent) {
        if (refreshSkinEvent.color != null) {
            statusBarColor = refreshSkinEvent.color;
            handleStatusBar(getStatusBarColor());
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentAndroidInjector;
    }

    protected boolean supportHandleStatusBar() {
        return true;
    }

    @ColorInt
    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void handleStatusBar(@ColorInt int statusBarColor) {
        StatusbarHelper.setStatusBarColor(this, statusBarColor);
    }
}
