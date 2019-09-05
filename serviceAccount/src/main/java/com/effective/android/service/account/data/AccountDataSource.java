package com.effective.android.service.account.data;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * account 模块数据接口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public interface AccountDataSource {

    void saveAccount(Oauth2AccessToken oauth2AccessToken);

    Oauth2AccessToken getAccount();

    void refreshAccount();
}
