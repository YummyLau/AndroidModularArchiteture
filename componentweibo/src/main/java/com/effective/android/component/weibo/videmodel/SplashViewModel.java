package com.effective.android.component.weibo.videmodel;

import android.accounts.Account;

import androidx.lifecycle.ViewModel;

import com.effective.android.base.rxjava.Rx2Creator;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class SplashViewModel extends ViewModel {

    public Flowable<Account> checkLoginStatus() {
        return Rx2Creator.createFlowable(new Callable<Account>() {
            @Override
            public Account call() throws Exception {
                return null;
            }
        });
//        return ServiceManager.getService(IAccountService.class)
//                .getAccount();
    }

}
