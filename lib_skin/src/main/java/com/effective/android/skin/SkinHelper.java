package com.effective.android.skin;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.effective.android.skin.bean.SkinItem;
import com.effective.android.skin.listener.OnSkinChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

import static com.effective.android.skin.Constants.SKIN_BLACK_NAME;
import static com.effective.android.skin.Constants.SKIN_BLUE_NAME;
import static com.effective.android.skin.Constants.SKIN_DEFAULT_NAME;


/**
 * 换肤助手
 * Created by yummyLau on 2018/9/6.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class SkinHelper {

    private static volatile SkinHelper INSTANCE = null;

    private boolean init;
    private Map<String, SkinItem> mSkinTypeMap;
    private List<OnSkinChangeListener> mSkinChanges;

    private SkinHelper() {
        mSkinTypeMap = new HashMap<>();
        mSkinChanges = new ArrayList<>();
        SkinItem defaultSkin = new SkinItem(SKIN_DEFAULT_NAME, Constants.SKIN_DISPLAY_DEFAULT_NAME);
        SkinItem blackSkin = new SkinItem(SKIN_BLACK_NAME, Constants.SKIN_DISPLAY_BLACK_NAME);
        SkinItem bluekSkin = new SkinItem(SKIN_BLUE_NAME, Constants.SKIN_DISPLAY_BLUE_NAME);
        mSkinTypeMap.put(defaultSkin.resourceName, defaultSkin);
        mSkinTypeMap.put(blackSkin.resourceName, blackSkin);
        mSkinTypeMap.put(bluekSkin.resourceName, bluekSkin);
    }

    public static SkinHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (SkinHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SkinHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Application application) {
        if (init) {
            return;
        }
        SkinCompatManager.withoutActivity(application)                  // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        init = true;
    }

    public void addSkinChangeListener(OnSkinChangeListener skinChange) {
        if (!init) {
            return;
        }
        if (skinChange != null && !mSkinChanges.contains(skinChange)) {
            mSkinChanges.add(skinChange);
        }
    }

    public void removeSkinChangeListener(OnSkinChangeListener skinChange) {
        if (!init) {
            return;
        }
        if (skinChange != null && mSkinChanges.contains(skinChange)) {
            mSkinChanges.remove(skinChange);
        }
    }

    public void notifySkinChange(boolean success, @Nullable SkinItem skin) {
        if (!init) {
            return;
        }
        for (OnSkinChangeListener iSkinChange : mSkinChanges) {
            iSkinChange.onChange(success, skin);
        }
    }

    public void changeSkin(final @NonNull SkinItem skin) {
        if (!init) {
            return;
        }
        if (skin != null) {
            SkinCompatManager.getInstance().loadSkin(skin.resourceName, new SkinCompatManager.SkinLoaderListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess() {
                    notifySkinChange(true, skin);
                }

                @Override
                public void onFailed(String errMsg) {
                    notifySkinChange(false, null);
                }
            }, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        }
    }

    public void nextSkin() {
        String name = SkinCompatManager.getInstance().getCurSkinName();
        switch (name) {
            case SKIN_DEFAULT_NAME: {
                changeSkin(mSkinTypeMap.get(SKIN_BLACK_NAME));
                break;
            }
            case SKIN_BLACK_NAME: {
                changeSkin(mSkinTypeMap.get(SKIN_BLUE_NAME));
                break;
            }
            case SKIN_BLUE_NAME: {
                changeSkin(mSkinTypeMap.get(SKIN_DEFAULT_NAME));
                break;
            }
        }
    }
}
