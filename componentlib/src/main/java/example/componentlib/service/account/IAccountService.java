package example.componentlib.service.account;


import example.componentlib.service.IService;
import io.reactivex.Flowable;

/**
 * account service that you can define some account rule!
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public interface IAccountService extends IService {

    void login(String returnActivityPath);

    void logout();

    void logout(boolean forceLogin);

    void logout(boolean forceLogin, String returnActivityPath);

    String getLoginPath();

    Flowable<Account> getAccount();
}
