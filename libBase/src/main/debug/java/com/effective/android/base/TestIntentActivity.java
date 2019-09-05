package com.effective.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.android.base.intent.IntentCompat;

import java.util.ArrayList;

public class TestIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_test_intent);
        Intent intent = IntentCompat.after(getIntent());
        TestIntentData one = intent.getParcelableExtra("1");
        TestIntentData[] two = (TestIntentData[]) intent.getParcelableArrayExtra("2");
        ArrayList<TestIntentData> three = intent.getParcelableArrayListExtra("3");
        Log.d("after testIntent-1", one.toString());
        Log.d("after testIntent-2", two.toString());
        Log.d("after testIntent-3", three.toString());
    }
}