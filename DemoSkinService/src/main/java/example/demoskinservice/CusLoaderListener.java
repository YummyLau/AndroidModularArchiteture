package example.demoskinservice;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import example.componentlib.service.skin.Skin;
import skin.support.SkinCompatManager;

/**
 * add a skin key to show log
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/24.
 */

public class CusLoaderListener implements SkinCompatManager.SkinLoaderListener {

    private Skin mSkin;

    public CusLoaderListener(@NonNull Skin skin) {
        mSkin = skin;
    }

    @Override
    public void onStart() {
        if (mSkin != null) {
            Log.d(Constants.LOG_TAG, "start load skin: " + mSkin.key);
        }
    }

    @Override
    public void onSuccess() {
        if (mSkin != null) {
            Log.d(Constants.LOG_TAG, "load skin " + mSkin.key + " success!");
        }
    }

    @Override
    public void onFailed(String errMsg) {
        if (mSkin != null) {
            Log.d(Constants.LOG_TAG, "load skin " + mSkin.key + " fail!");
            if (!TextUtils.isEmpty(errMsg)) {
                Log.d(Constants.LOG_TAG, errMsg);
            }
        }
    }
}
