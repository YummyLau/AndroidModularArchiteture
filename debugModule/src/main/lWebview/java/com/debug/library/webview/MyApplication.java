package com.debug.library.webview;

import android.support.multidex.MultiDexApplication;

import com.effective.android.x5js.X5WebUtils;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        X5WebUtils.initX5Webkit(this);
    }
}
