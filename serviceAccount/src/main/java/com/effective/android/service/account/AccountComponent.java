package com.effective.android.service.account;

import android.app.Application;

import com.effective.android.service.account.data.AccountRepository;
import com.plugin.component.IComponent;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class AccountComponent implements IComponent {

    public static AccountRepository accountRepository;

    @Override
    public void attachComponent(Application application) {
        WbSdk.install(this, new AuthInfo(application, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        accountRepository = new AccountRepository(application);
    }

    @Override
    public void detachComponent() {

    }

}
