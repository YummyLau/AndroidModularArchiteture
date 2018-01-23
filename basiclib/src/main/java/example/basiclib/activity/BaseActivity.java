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

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import example.basiclib.R;
import example.basiclib.util.StatusbarUtils;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 * {@link dagger.android.support.DaggerAppCompatActivity}
 */

public abstract class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public DB dataBinding;

    public VM viewModel;

    public abstract Class<VM> getViewModel();

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel());
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentAndroidInjector;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (supportHandlerStatusBar()) {
            setStatusBar();
        }
    }

    protected boolean supportHandlerStatusBar() {
        return true;
    }

    @ColorInt
    public int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    public void setStatusBar() {
        StatusbarUtils.setStatusbarColor(this, getStatusBarColor());
    }
}
