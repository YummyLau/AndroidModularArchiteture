package com.effective.android.service.media;


import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class TestApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
