package com.effective.android.component.weibo.videmodel;

import androidx.lifecycle.ViewModel;

import com.effective.android.component.weibo.WeiboComponent;
import com.effective.android.service.account.AccountResult;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class SplashViewModel extends ViewModel {

    public boolean isLogin() {
        return WeiboComponent.accountSdk.isLogin();
    }

    public void login(AccountResult accountResult) {
        WeiboComponent.accountSdk.login(accountResult);
    }
}
