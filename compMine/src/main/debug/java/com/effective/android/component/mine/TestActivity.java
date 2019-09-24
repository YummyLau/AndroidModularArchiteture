package com.effective.android.component.mine;

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
        setContentView(R.layout.mine_activity_main);
        ComponentManager.init(getApplication());
        ComponentMineSdk componentMineSdk = SdkManager.getSdk(ComponentMineSdk.class);
        FragmentUtils.add(getSupportFragmentManager(), R.id.id_fragment_container, componentMineSdk.getMainFragment(), "");
    }
}
