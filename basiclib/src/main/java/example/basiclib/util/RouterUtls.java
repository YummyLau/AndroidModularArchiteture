package example.basiclib.util;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由管理器
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class RouterUtls {

    public static void navigation(String path) {
        ARouter.getInstance().build(path).navigation();
    }
}
