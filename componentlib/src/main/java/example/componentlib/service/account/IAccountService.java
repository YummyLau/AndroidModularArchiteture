package example.componentlib.service.account;


import example.componentlib.service.IService;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * account service that you can define some account rule!
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public interface IAccountService extends IService {

    void login(String returnActivityPath);

    String getLoginPath();

    Observable<Account> getAccount();
}
