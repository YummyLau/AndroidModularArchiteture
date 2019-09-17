package com.effective.android.component.weibo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.plugin.component.ComponentManager;
import com.plugin.component.SdkManager;


public class WeiboActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_main);
        ComponentManager.init(getApplication());
        getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container,
                SdkManager.getSdk(WeiboSdk.class).getMainFragment()).commit();
    }
}
