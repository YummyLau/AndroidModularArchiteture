package com.effective.android.component.system;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.android.base.util.FragmentUtils;
import com.plugin.component.ComponentManager;
import com.plugin.component.SdkManager;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system__activity__main);
        ComponentManager.init(getApplication());
        ComponentSystemSdk systemSdk = SdkManager.getSdk(ComponentSystemSdk.class);
        FragmentUtils.replace(getSupportFragmentManager(), R.id.id_fragment_container, systemSdk.getMainFragment(), "");
    }
}
