package com.effective.android.service.skin

import android.app.Application
import android.graphics.Color
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent
import java.util.HashMap
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.design.app.SkinMaterialViewInflater

@AutoInjectComponent(impl = [SkinSdkImpl::class])
class SkinComponent : IComponent {

    override fun attachComponent(application: Application) {
        sSkinTypeMap[Constants.SUN_KEY] = Skin(Constants.SUN_KEY, "日间版本", Color.WHITE)
        sSkinTypeMap[Constants.NIGHT_KEY] = Skin(Constants.NIGHT_KEY, "夜间版本", Color.BLACK)

        SkinCompatManager.withoutActivity(application)
                .addInflater(SkinMaterialViewInflater())
                .addInflater(SkinCardViewInflater())
                .addInflater(SkinAppCompatViewInflater())
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
