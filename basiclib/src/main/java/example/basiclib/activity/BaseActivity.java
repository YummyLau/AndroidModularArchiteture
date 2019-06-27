package example.basiclib.activity;


import android.os.Bundle;


import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import example.basiclib.R;
import example.basiclib.event.RefreshSkinEvent;
import example.basiclib.util.EventBusUtils;
import example.basiclib.util.statusbar.StatusBarHelper;
import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public abstract class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity{

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
        super.onCreate(savedInstanceState);
        if (SkinCompatManager.getInstance() == null) {
            statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        } else {
            statusBarColor = SkinCompatResources.getColor(this, R.color.colorPrimary);
        }
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this).get(getViewModel());
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


    protected boolean supportHandleStatusBar() {
        return true;
    }

    @ColorInt
    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void handleStatusBar(@ColorInt int statusBarColor) {
        StatusBarHelper.setStatusBarColor(this, statusBarColor);
    }
}
