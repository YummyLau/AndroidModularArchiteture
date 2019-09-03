package com.effective.router.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.effective.router.core.Constants;
import com.effective.router.core.utils.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用于每个模块中如果使用到 {@link com.effective.router.core.Router} 注入，则为其host生成一个Router
 * 实现真正跳转逻辑
 * <p>
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public abstract class BaseComponentRouter implements IComponentRouter {

    protected Map<String, Class> routeMapper = new HashMap<>();

    protected Map<Class, Map<String, Integer>> paramsMapper = new HashMap<>();

    protected boolean hasInitMap = false;

    protected abstract String getHost();

    protected void initMap() {
        hasInitMap = true;
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, 0);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        return openUri(context, uri, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, requestCode);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
        if (!hasInitMap) {
            initMap();
        }
        if (uri == null || context == null) {
            return true;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (!getHost().equals(host)) {
            return false;
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        if (routeMapper.containsKey(path)) {
            Class target = routeMapper.get(path);
            if (bundle == null) {
                bundle = new Bundle();
            }
            Map<String, String> params = UriUtils.parseParams(uri);
            Map<String, Integer> paramsType = paramsMapper.get(target);
            UriUtils.setBundleValue(bundle, params, paramsType);
            Intent intent = new Intent(context, target);
            intent.putExtras(bundle);
            if (requestCode > 0 && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
                return true;
            }
            context.startActivity(intent);
            return true;
        } else {
            Log.w(Constants.LOG_TAG, "uri is not found : " + path);
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        String host = uri.getHost();
        if (!getHost().equals(host)) {
            return false;
        }
        if (!hasInitMap) {
            initMap();
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        return routeMapper.containsKey(path);
    }
}

