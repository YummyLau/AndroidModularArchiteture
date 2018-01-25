package example.componentlib.service;

import android.app.Application;

/**
 * 服务实现接口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public interface IService {

    void createAsLibrary(Application application);

    void release();
}
