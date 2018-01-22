package example.demoaccountservice;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
    }
}
