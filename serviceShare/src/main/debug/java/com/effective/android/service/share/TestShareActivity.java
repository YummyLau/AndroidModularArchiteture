package com.effective.android.service.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.effective.android.share.R;

public class TestShareActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_test);
    }
}
