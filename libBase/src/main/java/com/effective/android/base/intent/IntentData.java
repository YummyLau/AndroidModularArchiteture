package com.effective.android.base.intent;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IntentData {

    public static final String KEY = "IntentDataKey";

    private Map<String, Parcelable> data;
    private Map<String, Parcelable[]> arrayData;
    private Map<String, ArrayList<Parcelable>> arrayListData;
    public long value;

    public IntentData() {
        data = new HashMap<>();
        arrayData = new HashMap<>();
        arrayListData = new HashMap<>();
        value = System.currentTimeMillis();
    }

    public void put(String key, Parcelable value) {
        data.put(key, value);
    }

    public void put(String key, Parcelable[] value) {
        arrayData.put(key, value);
    }

    public void put(String key, ArrayList<Parcelable> value) {
        arrayListData.put(key, value);
    }

    public boolean isContainParcelableData() {
        return !data.isEmpty() || !arrayData.isEmpty() || !arrayListData.isEmpty();
    }

    public Intent make(Intent intent) {
        if (isContainParcelableData()) {
            intent.putExtra(KEY, value);
        }
        return intent;
    }

    public Intent reset(Intent intent) {
        if (!data.isEmpty()) {
            Set<String> keys = data.keySet();
            for (String key : keys) {
                intent.putExtra(key, data.get(key));
            }
        }
        if (!arrayData.isEmpty()) {
            Set<String> keys = arrayData.keySet();
            for (String key : keys) {
                intent.putExtra(key, arrayData.get(key));
            }
        }
        if (!arrayListData.isEmpty()) {
            Set<String> keys = arrayListData.keySet();
            for (String key : keys) {
                intent.putParcelableArrayListExtra(key, arrayListData.get(key));
            }
        }
        return intent;
    }
}
