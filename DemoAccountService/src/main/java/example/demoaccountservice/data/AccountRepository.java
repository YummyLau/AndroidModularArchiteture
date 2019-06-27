package example.demoaccountservice.data;

import android.app.Application;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;


import example.demoaccountservice.Constants;


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class AccountRepository implements AccountDataSource {

    private Application mApplication;

    public AccountRepository(Application application) {
        this.mApplication = application;
    }

    @Override
    public void saveAccount(Oauth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(mApplication, oauth2AccessToken);
        }
    }

    public Oauth2AccessToken getAccount() {
        return AccessTokenKeeper.readAccessToken(mApplication);
    }

    @Override
    public void refreshAccount() {
        if (!TextUtils.isEmpty(getAccount().getRefreshToken())) {
            AccessTokenKeeper.refreshToken(Constants.APP_KEY, mApplication, new RequestListener() {
                @Override
                public void onComplete(String s) {
                    //暂不需要处理
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    //暂不需要处理
                }
            });
        }
    }
}
