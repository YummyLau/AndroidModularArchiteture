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
    private val innerListener = InnerListener(this)

    private fun notifyChange(skin: Skin, success: Boolean) {
        for (skinChangeListener in listeners) {
            skinChangeListener.onSkinChange(skin, success)
        }
    }

    override fun changeSkin() {
        if (isLoadingDefaultSkin()) {
            changeSkin(SkinComponent.sSkinTypeMap[Constants.NIGHT_KEY]!!)
        } else {
            changeSkin(SkinComponent.sSkinTypeMap[Constants.SUN_KEY]!!)
        }
    }

    /**
     * demo只有白天夜间，通过 skin 可扩展多样式皮肤
     */
    override fun changeSkin(skin: Skin) {
        innerListener.attachSkin(skin)
        if (skin.key == Constants.SUN_KEY) {
            //SkinCompatManager.getInstance().restoreDefaultTheme() 没有监听器，所以notify中去获取当前皮肤可能存在同步问题，手动实现 restoreDefaultTheme 逻辑
            SkinCompatManager.getInstance().loadSkin("", innerListener, SkinCompatManager.SKIN_LOADER_STRATEGY_NONE)
        } else {
            SkinCompatManager.getInstance().loadSkin("night", innerListener, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
        }
    }

    override fun removeSkinChangeListener(skinChangeListener: SkinChangeListener?) {
        if (skinChangeListener != null && listeners.contains(skinChangeListener)) {
            listeners.remove(skinChangeListener)
        }
    }

    override fun addSkinChangeListener(skinChangeListener: SkinChangeListener?) {
        if (skinChangeListener != null && !listeners.contains(skinChangeListener)) {
            listeners.add(skinChangeListener)
        }
    }

    override fun getCurSkin(): Skin {
        return if (SkinCompatManager.getInstance().curSkinName == Constants.NIGHT_KEY) {
            SkinComponent.sSkinTypeMap[Constants.NIGHT_KEY]!!
        } else {
            SkinComponent.sSkinTypeMap[Constants.SUN_KEY]!!
        }
    }

    override fun isLoadingDefaultSkin(): Boolean {
        return SkinCompatManager.getInstance().curSkinName == ""
    }

    override fun getSkins(): List<Skin> {
        val skins = mutableListOf<Skin>()
        for (key in SkinComponent.sSkinTypeMap.keys) {
            if (key != Constants.SUN_KEY) {
                skins.add(SkinComponent.sSkinTypeMap[key]!!)
            } else {
                skins.add(0, SkinComponent.sSkinTypeMap[key]!!)
            }
        }
        return skins
    }

    private class InnerListener(val impl: SkinSdkImpl) : SkinCompatManager.SkinLoaderListener {

        private var skin: Skin? = null

        fun attachSkin(skin: Skin) {
            this.skin = skin
        }

        override fun onSuccess() {

            if (skin != null) {
                Log.d(Constants.LOG_TAG, "load skin " + skin!!.key + " success!")
                impl.notifyChange(skin!!, true)
            }
        }

        override fun onFailed(errMsg: String?) {
            if (skin != null) {
                Log.d(Constants.LOG_TAG, "load skin " + skin!!.key + " fail!")
                if (!TextUtils.isEmpty(errMsg)) {
                    Log.d(Constants.LOG_TAG, errMsg)
                }
                impl.notifyChange(skin!!, false)
            }
        }

        override fun onStart() {
            if (skin != null) {
                Log.d(Constants.LOG_TAG, "start load skin: " + skin!!.key)
            }
        }
    }
}
