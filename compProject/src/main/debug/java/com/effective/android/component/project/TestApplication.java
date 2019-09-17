package com.effective.android.component.project;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class TestApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
