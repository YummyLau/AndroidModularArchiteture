package com.effective.router.core.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.effective.router.annotation.RouteUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 只有在这里注入，才会对{@link BaseComponentRouter}的子类进行反射并初始化到路由表中
 * <p>
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class UIRouter implements IUIRouter {

    private static Map<String, IComponentRouter> routerInstanceCache = new HashMap<>();

    private List<IComponentRouter> uiRouters = new ArrayList<>();
    private HashMap<IComponentRouter, Integer> priorities = new HashMap<>();

    private static volatile UIRouter instance;

    private UIRouter() {
    }

    public static UIRouter getInstance() {
        if (instance == null) {
            synchronized (UIRouter.class) {
                if (instance == null) {
                    instance = new UIRouter();
                }
            }
        }
        return instance;
    }


    @Override
    public void registerUI(IComponentRouter router, int priority) {
        //如果当前存在且优先级一致，则默认已经注册过
        if (priorities.containsKey(router) && priority == priorities.get(router)) {
            return;
        }
        //删除旧的注册
        removeOldUIRouter(router);

        //按照优先级插入路由表
        int i = 0;
        for (IComponentRouter temp : uiRouters) {
            Integer tp = priorities.get(temp);
            if (tp == null || tp <= priority) {
                break;
            }
            i++;
        }
        uiRouters.add(i, router);
        priorities.put(router, priority);
    }

    @Override
    public void registerUI(IComponentRouter router) {
        registerUI(router, PRIORITY_NORMAL);
    }

    @Override
    public void registerUI(String host) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            registerUI(router, PRIORITY_NORMAL);
        }
    }

    @Override
    public void registerUI(String host, int priority) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            registerUI(router, priority);
        }
    }

    @Override
    public void unregisterUI(IComponentRouter router) {
        for (int i = 0; i < uiRouters.size(); i++) {
            if (router == uiRouters.get(i)) {
                uiRouters.remove(i);
                priorities.remove(router);
                break;
            }
        }
    }

    @Override
    public void unregisterUI(String host) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            unregisterUI(router);
        }
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        return openUri(context, url, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        return openUri(context, uri, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
        url = url.trim();
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("://")
                    && (!url.startsWith("tel:") || !url.startsWith("smsto:") || !url.startsWith("file:"))) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            return openUri(context, uri, bundle, requestCode);
        }
        return true;
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
        for (IComponentRouter temp : uiRouters) {
            try {
                if (temp.verifyUri(uri) && temp.openUri(context, uri, bundle, requestCode)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        for (IComponentRouter temp : uiRouters) {
            if (temp.verifyUri(uri)) {
                return true;
            }
        }
        return false;
    }


    private void removeOldUIRouter(IComponentRouter router) {
        Iterator<IComponentRouter> iterator = uiRouters.iterator();
        while (iterator.hasNext()) {
            IComponentRouter tmp = iterator.next();
            if (tmp == router) {
                iterator.remove();
                priorities.remove(tmp);
            }
        }
    }

    /**
     * 查找是否存在该host
     *
     * @param host
     * @return
     */
    private IComponentRouter fetch(@NonNull String host) {

        //获取生成类class路径
        String path = RouteUtils.genHostUIRouterClass(host);

        if (routerInstanceCache.containsKey(path))
            return routerInstanceCache.get(path);

        try {
            Class cla = Class.forName(path);
            IComponentRouter instance = (IComponentRouter) cla.newInstance();
            routerInstanceCache.put(path, instance);
            return instance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
