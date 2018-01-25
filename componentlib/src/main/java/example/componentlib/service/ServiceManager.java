package example.componentlib.service;


import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一模块化service入口
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ServiceManager {

    private static final String TAG = ServiceManager.class.getSimpleName();

    private static Map<String, IService> sServiceMap = new HashMap<>();

    public static <T extends IService> void register(@NonNull Application application, @NonNull Class<T> service) {
        IService registerService = null;
        try {
            Constructor constructor = service.getConstructor();
            registerService = (IService) constructor.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "bind fail!:" + e.getMessage());
        }
        Class IServiceImpl = ifClassImplementsIService(service.getInterfaces());
        if (IServiceImpl != null) {
            sServiceMap.put(IServiceImpl.getSimpleName(), registerService);
            registerService.createAsLibrary(application);
        } else {
            Log.e(TAG, "IService is not service's grandfather!");
        }
    }


    public static Class ifClassImplementsIService(Class[] interfaces) {
        int length = interfaces.length;
        int i = 0;
        while (i < length) {
            Class result = ifClassImplementsIService(interfaces[i]);
            if (result != null) {
                return result;
            }
            if (ifClassHadImplementsInterface(interfaces[i])) {
                return ifClassImplementsIService(interfaces[i].getInterfaces());
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

    public static Class ifClassImplementsIService(Class interfaces) {
        Class[] classes = interfaces.getInterfaces();
        if (classes != null && classes.length > 0) {
            for (int i = 0; i < classes.length; i++) {
                if (classes[0].getSimpleName().equals(IService.class.getSimpleName())) {
                    return interfaces;
                }
            }
        }
        return null;
    }


    @Nullable
    public static <T extends IService> T getService(@NonNull Class<T> service) {
        IService result = null;
        if (service != null) {
            result = sServiceMap.get(service.getSimpleName());
        }
        if (result == null) {
            Log.w(TAG, "has not service which match a servicePath key!");
        }
        return (T) result;
    }

    public static <T extends IService> void unRegister(@NonNull Class<T> service) {
        if (service == null) {
            Log.w(TAG, "service class can't be null");
            return;
        }
        T toUnRegister = (T) sServiceMap.remove(service.getSimpleName());
        toUnRegister.release();
    }

}
