package com.effective.android.base.intent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * 为了解决 intent使用传递过大数据，全局统一处理，避免后续还发流程中传递
 * {@link Parcelable} 相关数据随着迭代不断膨胀，crash 隐藏风险增大。
 * <p>
 * 方案1，使用继承 intent 拦截 {@link Parcelable} 相关入口，自定义保存 {@link Parcelable} 相关数据，在 getIntent 方法中统一返回子类
 * 方案2，使用原 intent ，在startActivity等场景使用 intent 前取出在bundle中 {@link Parcelable}相关数据 ，在 getIntent中恢复
 * <p>
 * 鉴于兼容旧版本逻辑，采用方案2
 * created by yummylau 2019/09/05
 */
public class IntentCompat {

    private static Set<IntentData> intentDataSet = new HashSet<>();

    public static Intent before(Intent intent) {
        IntentData intentData = new IntentData();
        Bundle bundle = intent.getExtras();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            Object object = bundle.get(key);
            if (object instanceof Parcelable) {
                intent.removeExtra(key);
                intentData.put(key, (Parcelable) object);
            }

            if (object instanceof Parcelable[]) {
                intent.removeExtra(key);
                intentData.put(key, (Parcelable[]) object);
            }

            if (object instanceof ArrayList) {
                if (!((ArrayList) object).isEmpty()) {
                    if (((ArrayList) object).get(0) instanceof Parcelable) {
                        intent.removeExtra(key);
                        intentData.put(key, (ArrayList<Parcelable>) object);
                    }
                }
            }
        }
        if (intentData.isContainParcelableData()) {
            intentDataSet.add(intentData);
        }
        return intentData.make(intent);
    }

    @Nullable
    private static IntentData getIntentData(@NonNull Intent intent) {
        IntentData result = null;
        Bundle bundle = intent.getExtras();
        Set<String> keys = bundle.keySet();
        if (keys.contains(IntentData.KEY)) {
            long value = bundle.getLong(IntentData.KEY);
            intent.removeExtra(IntentData.KEY);
            for (IntentData intentData : intentDataSet) {
                if (intentData.value == value) {
                    result = intentData;
                    intentDataSet.remove(result);
                    break;
                }
            }
        }
        return result;
    }


    public static Intent after(Intent intent) {
        IntentData intentData = getIntentData(intent);
        if (intentData != null) {
            intentData.reset(intent);
        }
        return intent;
    }
}
