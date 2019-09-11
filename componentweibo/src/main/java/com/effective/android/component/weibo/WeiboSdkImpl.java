package com.effective.android.component.weibo;

import androidx.fragment.app.Fragment;

import com.plugin.component.anno.AutoInjectImpl;

import org.jetbrains.annotations.NotNull;

@AutoInjectImpl(sdk = WeiboSdk.class)
public class WeiboSdkImpl implements WeiboSdk {

    @NotNull
    @Override
    public String getMainPath() {
        return null;
    }

    @Override
    public void gotoMainActivity() {

    }

    @NotNull
    @Override
    public Fragment getWeiboMainFragment() {
        return null;
    }
}
