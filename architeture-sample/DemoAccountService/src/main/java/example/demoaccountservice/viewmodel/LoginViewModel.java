package example.demoaccountservice.viewmodel;

import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;


import androidx.lifecycle.ViewModel;
import example.basiclib.util.EventBusUtils;
import example.componentlib.service.account.AccountEvent;
import example.demoaccountservice.App;

import static example.demoaccountservice.Constants.LOG_TAG;


/**
 * 登录模块viewmodel
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class LoginViewModel extends ViewModel {

    public void onClickToLogin(SsoHandler ssoHandler) {
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                App.accountRepository.saveAccount(oauth2AccessToken);
                EventBusUtils.post(new AccountEvent(AccountEvent.LOGIN_TYPE));
                Log.d(LOG_TAG, "login success!");
            }

            @Override
            public void cancel() {
                Log.d(LOG_TAG, "login cancel!");
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                Log.d(LOG_TAG, "login fail: " + wbConnectErrorMessage.getErrorMessage());
            }
        });
    }
}
