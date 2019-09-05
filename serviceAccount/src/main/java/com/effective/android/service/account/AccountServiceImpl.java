package com.effective.android.service.account;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import example.basiclib.util.EventBusUtils;
import example.componentlib.service.account.Account;
import example.componentlib.service.account.AccountEvent;
import example.componentlib.service.account.IAccountService;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class AccountServiceImpl implements IAccountService {

    public Context mApplication;

    @Override
    public void login(String returnActivityPath) {
        ARouter.getInstance().build(getLoginPath())
                .withString(Constants.RETURN_ACTIVITY_PATH, returnActivityPath)
                .navigation();
    }

    @Override
    public void logout() {
        logout(false);
    }

    @Override
    public void logout(boolean forceLogin) {
        logout(forceLogin, null);
    }

    @Override
    public void logout(boolean forceLogin, String returnActivityPath) {
        AccessTokenKeeper.clear(mApplication);
        EventBusUtils.post(new AccountEvent(AccountEvent.LOGOUT_TYPE));
        if (forceLogin) {
            Postcard postcard = ARouter.getInstance().build(getLoginPath());
            if (!TextUtils.isEmpty(returnActivityPath)) {
                postcard.withString(Constants.RETURN_ACTIVITY_PATH, returnActivityPath);
            }
            postcard.navigation();
        }
    }

    @Override
    public Flowable<Account> getAccount() {
        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
                .map(new Function<Oauth2AccessToken, Account>() {
                    @Override
                    public Account apply(Oauth2AccessToken oauth2AccessToken) throws Exception {
                        Account account = null;
                        if (!oauth2AccessToken.isSessionValid()) {
                            Log.e(Constants.LOG_TAG, "AccountServiceImpl#getAccount token invalid!");
                        } else {
                            account = new Account();
                            account.uid = Integer.valueOf(oauth2AccessToken.getUid());
                            account.token = oauth2AccessToken.getToken();
                            account.refreshToken = oauth2AccessToken.getRefreshToken();
                            account.expiresTime = oauth2AccessToken.getExpiresTime();
                            account.phoneNum = oauth2AccessToken.getPhoneNum();
                        }
                        return account;
                    }
                });
    }

    @Override
    public String getLoginPath() {
        return Constants.ROUTER_LOGIN;
    }


    @Override
    public void createAsLibrary(Application application) {
        mApplication = application;
        WbSdk.install(application, new AuthInfo(application, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
    }

    @Override
    public void release() {
        mApplication = null;
    }
}
