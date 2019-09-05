package com.effective.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.android.base.intent.IntentCompat;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);
        testIntent();
    }


    private void testIntent() {
        findViewById(R.id.testIntent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int byteCount = 2 * 1024 * 1024;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < byteCount; i++) {
                    stringBuffer.append("a");
                }
                String longData = stringBuffer.toString();
                Intent intent = new Intent(TestActivity.this, TestIntentActivity.class);
                TestIntentData one = new TestIntentData(longData);
                TestIntentData[] two = new TestIntentData[]{new TestIntentData(longData)};
                ArrayList<TestIntentData> three = new ArrayList<>();
                three.add(new TestIntentData(longData));
                three.add(new TestIntentData(longData));
                intent.putExtra("1", one);
                intent.putExtra("2",two );
                intent.putExtra("3", three);
                Log.d("testIntent-1",one.toString());
                Log.d("testIntent-2",two.toString());
                Log.d("testIntent-3",three.toString());
                TestActivity.this.startActivity(IntentCompat.before(intent));
            }
        });
    }
}
