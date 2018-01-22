package example.basiclib.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import example.basiclib.R;
import example.basiclib.util.StatusbarUtil;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/02.
 */

public abstract class BaseBindingActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    public DB dataBinding;

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
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
        StatusbarUtil.setStatusbarColor(this, getStatusBarColor());
    }
}
