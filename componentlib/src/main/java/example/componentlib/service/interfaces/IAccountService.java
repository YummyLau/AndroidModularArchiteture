package example.componentlib.service.interfaces;


import example.componentlib.service.IService;

/**
 * account service that you can define some account rule!
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public interface IAccountService extends IService {

    void login(String returnActivityPath);

    boolean isLogin();
}
