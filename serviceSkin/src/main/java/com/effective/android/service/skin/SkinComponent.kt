package com.effective.android.service.skin

import android.app.Application
import android.graphics.Color
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent
import java.util.HashMap
import skin.support.SkinCompatManager
import skin.support.app.SkinCardViewInflater
import skin.support.design.app.SkinMaterialViewInflater

@AutoInjectComponent(impl = [SkinSdkImpl::class])
class SkinComponent : IComponent {

    override fun attachComponent(application: Application) {
        sSkinTypeMap.put(Constants.DEFAULT_KEY, Skin(Constants.DEFAULT_KEY, "", Color.parseColor("#00BCD4")))
        sSkinTypeMap.put(Constants.BLUE_GREY_KEY, Skin(Constants.BLUE_GREY_KEY, "blueGrey.skin", Color.parseColor("#607D8B")))
        sSkinTypeMap.put(Constants.GREEN_KEY, Skin(Constants.GREEN_KEY, "green.skin", Color.parseColor("#4CAF50")))
        sSkinTypeMap.put(Constants.PICK_KEY, Skin(Constants.PICK_KEY, "pick.skin", Color.parseColor("#E91E63")))

        SkinCompatManager.withoutActivity(application)
                .addInflater(SkinMaterialViewInflater())      // 基础控件换肤初始化
                .addInflater(SkinCardViewInflater())          // material design 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)
                .setSkinWindowBackgroundEnable(false)
                .loadSkin()
    }

    override fun detachComponent() {
        sSkinTypeMap.clear()
    }

    companion object {
        val sSkinTypeMap: MutableMap<String, Skin> = HashMap()
    }
}
