package example.androidmodulararchiteture.viewmodel;

import android.arch.lifecycle.ViewModel;
import javax.inject.Inject;
import example.componentlib.service.ServiceManager;
import example.componentlib.service.account.Account;
import example.componentlib.service.account.IAccountService;
import io.reactivex.Flowable;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/22.
 */

public class SplashViewModel extends ViewModel {

    @Inject
    public SplashViewModel() {

    }

    public Flowable<Account> checkLoginStatus() {
        return ServiceManager.getService(IAccountService.class)
                .getAccount();
    }

}
