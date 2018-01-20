package example.componentlib.component;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import example.componentlib.service.ServiceManager;

/**
 * 组件注册管理
 * Email yummyl.lau@gmail.com
 * Created by g8931 on 2018/1/17.
 */

public class ComponentManager {

    private static final String TAG = ServiceManager.class.getSimpleName();

    private static Map<String, IComponent> sComponentMap = new HashMap<>();

    public static <T extends IComponent> void bind(@NonNull Application application, @NonNull Class<T> service) {
        IComponent registerService = null;
        try {
            Constructor constructor = service.getConstructor();
            registerService = (IComponent) constructor.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "bind fail!:" + e.getMessage());
        }
        if (registerService != null) {
            sComponentMap.put(service.getSimpleName(), registerService);
            registerService.createAsLibrary(application);
        }

        Class IComponentImpl = ifClassImplementsIComponent(service.getInterfaces());
        if (IComponentImpl != null) {
            sComponentMap.put(IComponentImpl.getSimpleName(), registerService);
            registerService.createAsLibrary(application);
        } else {
            Log.e(TAG, "IService is not service's grandfather!");
        }
    }


    public static Class ifClassImplementsIComponent(Class[] interfaces) {
        int length = interfaces.length;
        int i = 0;
        while (i < length) {
            Class result = ifClassImplementsIComponent(interfaces[i]);
            if (result != null) {
                return result;
            }
            if (ifClassHadImplementsInterface(interfaces[i])) {
                return ifClassImplementsIComponent(interfaces[i].getInterfaces());
            }
            i++;
        }
        return null;
    }

    public static boolean ifClassHadImplementsInterface(Class clazz) {
        boolean result = false;
        if (clazz != null && clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
            result = true;
        }
        return result;
    }

    public static Class ifClassImplementsIComponent(Class interfaces) {
        Class[] classes = interfaces.getInterfaces();
        if (classes != null && classes.length > 0) {
            for (int i = 0; i < classes.length; i++) {
                if (classes[0].getSimpleName().equals(IComponent.class.getSimpleName())) {
                    return interfaces;
                }
            }
        }
        return null;
    }


    @Nullable
    public static <T extends IComponent> T getComponent(@NonNull Class<T> service) {
        IComponent result = null;
        if (service != null) {
            result = sComponentMap.get(service.getSimpleName());
        }
        if (result == null) {
            Log.w(TAG, "has not component which match a servicePath key!");
        }
        return (T) result;
    }

    public static <T extends IComponent> void unbind(@NonNull Class<T> service) {
        if (service == null) {
            Log.w(TAG, "component class can't be null");
            return;
        }
        T toUnRegister = (T) sComponentMap.remove(service.getSimpleName());
        toUnRegister.release();
    }
}
