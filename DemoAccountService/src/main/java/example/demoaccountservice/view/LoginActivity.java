package example.demoaccountservice.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import example.basiclib.activity.BaseActivity;
import example.basiclib.util.EventBusUtils;
import example.basiclib.util.FontUtils;
import example.componentlib.service.account.AccountEvent;
import example.demoaccountservice.Constants;
import example.demoaccountservice.R;
import example.demoaccountservice.databinding.AccountActivityLoginLayoutBinding;
import example.demoaccountservice.viewmodel.LoginViewModel;

/**
 * login activity
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Route(path = Constants.ROUTER_LOGIN)
public class LoginActivity extends BaseActivity<LoginViewModel, AccountActivityLoginLayoutBinding> {

    private String returnPath;
    private SsoHandler mSsoHandler;

    @Override
    public Class<LoginViewModel> getViewModel() {
        return LoginViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.account_activity_login_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        returnPath = getIntent().getStringExtra(Constants.RETURN_ACTIVITY_PATH);
        mSsoHandler = new SsoHandler(this);
        dataBinding.setViewmodel(viewModel);
        dataBinding.setSsohandler(mSsoHandler);
        initView();
    }

    private void initView() {
        FontUtils.getInstance().replaceFontFromAsset(dataBinding.title, "fonts/DIN-Condensed-Bold-2.ttf");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果发起sso授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AccountEvent event) {
        if (event.type == AccountEvent.LOGIN_TYPE) {
            if (!TextUtils.isEmpty(returnPath)) {
                ARouter.getInstance().build(returnPath).navigation();
            }
            finish();
        }
    }
}
