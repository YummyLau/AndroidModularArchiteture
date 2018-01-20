package example.basiclib.util;


import org.greenrobot.eventbus.EventBus;

/**
 * module内驱动
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class EventbusUtils {

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unRegister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

}
