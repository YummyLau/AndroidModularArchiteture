package example.demoaccountservice.data;

import android.app.Application;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.demoaccountservice.Constants;


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/28.
 */
@Singleton
public class AccountRepository implements AccountDataSource {

    private Application mApplication;

    @Inject
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

    //    @Override
//    public Flowable<Boolean> isLogin() {
//        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
//                .map(new Function<Oauth2AccessToken, Boolean>() {
//                    @Override
//                    public Boolean apply(Oauth2AccessToken oauth2AccessToken) throws Exception {
//                        return oauth2AccessToken != null && oauth2AccessToken.isSessionValid();
//                    }
//                });
//    }
//
//    @Override
//    public Flowable<Token> getAccount() {
//        return Flowable.just(AccessTokenKeeper.readAccessToken(mApplication))
//                .map(new Function<Oauth2AccessToken, Token>() {
//                    @Override
//                    public Token apply(Oauth2AccessToken oauth2AccessToken) throws Exception {
//                        if (!oauth2AccessToken.isSessionValid()) {
//                            throw new TokenInvalidException();
//                        }
//                        Token token = new Token();
//                        token.uid = Integer.valueOf(oauth2AccessToken.getUid());
//                        token.accessToken = oauth2AccessToken.getAccount();
//                        token.refreshToken = oauth2AccessToken.getRefreshToken();
//                        token.expiresTime = oauth2AccessToken.getExpiresTime();
//                        token.phoneNum = oauth2AccessToken.getPhoneNum();
//                        return token;
//                    }
//                });
//    }
}
