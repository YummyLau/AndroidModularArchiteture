package com.effective.android.component.weibo;

import androidx.lifecycle.ViewModel;

import example.componentlib.service.ServiceManager;
import example.componentlib.service.account.Account;
import example.componentlib.service.account.IAccountService;
import io.reactivex.Flowable;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class SplashViewModel extends ViewModel {


    public Flowable<Account> checkLoginStatus() {
        return ServiceManager.getService(IAccountService.class)
                .getAccount();
    }

}
