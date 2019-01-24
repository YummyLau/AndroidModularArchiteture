package example.basiclib.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * activity工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ActivityUtils {

    public static Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }
}
