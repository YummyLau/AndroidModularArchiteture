package com.effective.android.service.skin

import android.text.TextUtils
import android.util.Log
import com.plugin.component.anno.AutoInjectImpl
import skin.support.SkinCompatManager

/**
 * demo provide blue skin
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@AutoInjectImpl(sdk = [ServiceSkin::class])
class SkinSdkImpl : ServiceSkin {

    private val listeners = mutableListOf<SkinChangeListener>()

    private fun notifyChange(skin: Skin, success: Boolean) {
        for (skinChangeListener in listeners) {
            skinChangeListener.onSkinChange(skin, success)
        }
    }

    override fun changeSkin(skin: Skin) {
        if (skin.key == Constants.DEFAULT_KEY) {
            SkinCompatManager.getInstance().restoreDefaultTheme()
            notifyChange(skin, true)
        } else {
            SkinCompatManager.getInstance().loadSkin(skin.sourceName, object : SkinCompatManager.SkinLoaderListener {
                override fun onStart() {
                    Log.d(Constants.LOG_TAG, "start load skin: " + skin.key)
                }

                override fun onSuccess() {
                    Log.d(Constants.LOG_TAG, "load skin " + skin.key + " success!")
                    notifyChange(skin, true)
                }

                override fun onFailed(errMsg: String) {
                    Log.d(Constants.LOG_TAG, "load skin " + skin.key + " fail!")
                    if (!TextUtils.isEmpty(errMsg)) {
                        Log.d(Constants.LOG_TAG, errMsg)
                    }
                    notifyChange(skin, false)
                }
            }, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS)
        }
    }

    override fun removeSkinChangeListener(skinChangeListener: SkinChangeListener) {
        listeners.remove(skinChangeListener)
    }

    override fun addSkinChangeListener(skinChangeListener: SkinChangeListener) {
        listeners.add(skinChangeListener)
    }


    override fun getSkins(): List<Skin> {
        val skins = mutableListOf<Skin>()
        for (key in SkinComponent.sSkinTypeMap.keys) {
            if (key != Constants.DEFAULT_KEY) {
                skins.add(SkinComponent.sSkinTypeMap[key]!!)
            } else {
                skins.add(0, SkinComponent.sSkinTypeMap[key]!!)
            }
        }
        return skins
    }
}
