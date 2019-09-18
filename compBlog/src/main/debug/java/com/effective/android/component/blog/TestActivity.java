package com.effective.android.component.blog;

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
        setContentView(R.layout.csystem_activitt__main);
        ComponentManager.init(getApplication());
        ComponentBlogSdk componentBlogSdk = SdkManager.getSdk(ComponentBlogSdk.class);
        FragmentUtils.add(getSupportFragmentManager(), R.id.id_fragment_container, componentBlogSdk.getMainFragment(), null);
    }
}
