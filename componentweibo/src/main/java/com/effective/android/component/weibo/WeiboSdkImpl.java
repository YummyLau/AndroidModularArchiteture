package com.effective.android.component.weibo;

import androidx.fragment.app.Fragment;

import com.effective.android.component.weibo.view.fragment.WeiboMainFragment;
import com.plugin.component.anno.AutoInjectImpl;

import org.jetbrains.annotations.NotNull;

@AutoInjectImpl(sdk = WeiboSdk.class)
public class WeiboSdkImpl implements WeiboSdk {

    WeiboMainFragment fragment;

    @NotNull
    @Override
    public Fragment getMainFragment() {
        if (fragment == null) {
            fragment = new WeiboMainFragment();
        }
        return fragment;
    }
}
