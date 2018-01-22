package example.demoaccountservice;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import example.componentlib.service.account.Account;
import example.componentlib.service.account.IAccountService;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 *
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
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
    public Observable<Account> getAccount() {
        return Observable.just(AccessTokenKeeper.readAccessToken(mApplication))
                .map(new Function<Oauth2AccessToken, Account>() {
                         @Override
                         public Account apply(Oauth2AccessToken oauth2AccessToken) throws Exception {
                             Account account = null;
                             if (!oauth2AccessToken.isSessionValid()) {
                                 Log.e(Constants.LOG_TAG, "AccountServiceImpl#getAccount token invalid!");
                             } else {
                                 account = new Account();
                             }
                             account.uid = Integer.valueOf(oauth2AccessToken.getUid());
                             account.token = oauth2AccessToken.getToken();
                             account.refreshToken = oauth2AccessToken.getRefreshToken();
                             account.expiresTime = oauth2AccessToken.getExpiresTime();
                             account.phoneNum = oauth2AccessToken.getPhoneNum();
                             return account;
                         }
                     }
                );
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
