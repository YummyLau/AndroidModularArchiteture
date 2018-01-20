package example.basiclib.util;

import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 魅族转有 =。=
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public class FlymeUtils {

    /**
     * 魅族Uitl
     * @return
     */
    public static boolean hasSmartBar() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return (Boolean) method.invoke(null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void hideSmartBar(View decorView) {
        if (hasSmartBar()) {
            try {
                @SuppressWarnings("rawtypes")
                Class[] arrayOfClass = new Class[1];
                arrayOfClass[0] = Integer.TYPE;
                Method localMethod = View.class.getMethod("setSystemUiVisibility",
                        arrayOfClass);
                Field localField = View.class
                        .getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
                Object[] arrayOfObject = new Object[1];
                try {
                    arrayOfObject[0] = localField.get(null);
                } catch (Exception e) {

                }
                localMethod.invoke(decorView, arrayOfObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
