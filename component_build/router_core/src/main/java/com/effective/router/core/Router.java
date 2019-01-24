package com.effective.router.core;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * 用于动态注册服务
 * <p>
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class Router {

    private HashMap<String, Object> services = new HashMap<>();
    //注册的组件的集合
    private static HashMap<String, IComponentLike> components = new HashMap<>();

    private static volatile Router instance;

    private Router() {
    }

    public static Router getInstance() {
        if (instance == null) {
            synchronized (Router.class) {
                if (instance == null) {
                    instance = new Router();
                }
            }
        }
        return instance;
    }


    public synchronized void addService(String serviceName, Object serviceImpl) {
        if (serviceName == null || serviceImpl == null) {
            return;
        }
        services.put(serviceName, serviceImpl);
    }

    public synchronized Object getService(String serviceName) {
        if (serviceName == null) {
            return null;
        }
        return services.get(serviceName);
    }

    public synchronized void removeService(String serviceName) {
        if (serviceName == null) {
            return;
        }
        services.remove(serviceName);
    }

    /**
     * 注册组件
     *
     * @param classname 组件名
     */
    public static void registerComponent(@Nullable String classname) {
        if (TextUtils.isEmpty(classname)) {
            return;
        }
        if (components.keySet().contains(classname)) {
            return;
        }
        try {
            Class clazz = Class.forName(classname);
            IComponentLike componentLike = (IComponentLike) clazz.newInstance();
            componentLike.onCreate();
            components.put(classname, componentLike);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反注册组件
     *
     * @param classname 组件名
     */
    public static void unregisterComponent(@Nullable String classname) {
        if (TextUtils.isEmpty(classname)) {
            return;
        }
        if (components.keySet().contains(classname)) {
            components.get(classname).onRelease();
            components.remove(classname);
            return;
        }
        try {
            Class clazz = Class.forName(classname);
            IComponentLike componentLike = (IComponentLike) clazz.newInstance();
            componentLike.onRelease();
            components.remove(classname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
