package example.componentlib.component;

import android.app.Application;

/**
 * 基础组件实现接口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public interface IComponent {

    void createAsLibrary(Application application);

    void release();
}



