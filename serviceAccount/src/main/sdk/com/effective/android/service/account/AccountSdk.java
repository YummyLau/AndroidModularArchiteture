package com.effective.android.service.account;

import io.reactivex.Flowable;

public interface AccountSdk {

    void login(String returnActivityPath);

    void logout();

    void logout(boolean forceLogin);

    void logout(boolean forceLogin, String returnActivityPath);

    String getLoginPath();

    Flowable<Account> getAccount();
}
