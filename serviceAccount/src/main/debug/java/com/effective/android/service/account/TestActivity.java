package com.effective.android.service.account;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.android.base.rxjava.Rx2Schedulers;
import com.effective.android.base.toast.ToastUtils;
import com.effective.android.base.util.GsonUtils;
import com.plugin.component.ComponentManager;
import com.plugin.component.SdkManager;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TestActivity extends AppCompatActivity implements AccountChangeListener {

    private UserInfo userInfo;
    private Disposable disposable;
    private AccountSdk accountSdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_main);
        init();
    }

    private void init() {
        ComponentManager.init(getApplication());
        accountSdk = SdkManager.getSdk(AccountSdk.class);
        userInfo = accountSdk.getAccount();
        accountSdk.addAccountChangeListener(this);
        checkoutStatus(userInfo != null, userInfo);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSdk.login(TestActivity.this);
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSdk.logout();
            }
        });
    }

    @Override
    public void onAccountChange(@org.jetbrains.annotations.Nullable UserInfo userInfo, boolean login, boolean success, @org.jetbrains.annotations.Nullable String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    checkoutStatus(login, userInfo);
                }
                ToastUtils.show(TestActivity.this, message);
            }
        });
    }

    private void checkoutStatus(boolean login, UserInfo userInfo) {
        findViewById(R.id.login).setEnabled(!login);
        findViewById(R.id.logout).setEnabled(login);
        ((TextView) findViewById(R.id.info)).setText(login ? GsonUtils.getJsonString(userInfo) : "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (accountSdk != null) {
            accountSdk.removeAccountChangeListener(this);
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
