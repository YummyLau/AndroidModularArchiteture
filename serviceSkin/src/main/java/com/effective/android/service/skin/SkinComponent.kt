package com.effective.android.service.skin

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.effective.android.base.util.ResourceHandler
import com.effective.android.base.util.ResourceUtils
import com.plugin.component.IComponent
import com.plugin.component.anno.AutoInjectComponent
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.content.res.SkinCompatResources
import skin.support.design.app.SkinMaterialViewInflater
import java.util.*

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

        ResourceUtils.setResourceProxy(object : ResourceHandler {

            override fun getColor(context: Context, color: Int): Int {
                return SkinCompatResources.getColor(context, color)
            }

            override fun getDrawable(context: Context, drawable: Int): Drawable? {
                return SkinCompatResources.getDrawable(context, drawable)
            }

            override fun getDrawable(context: Context, name: String): Drawable? {
                return ContextCompat.getDrawable(context, context.resources.getIdentifier(name, "drawable", context.applicationInfo.packageName))
            }

            override fun getString(context: Context, string: Int): String = context.getString(string)

            override fun getText(context: Context, string: Int): CharSequence = context.getText(string)

            override fun formatString(context: Context, string: Int, targetSting: String): String = String.format(context.getString(string), targetSting.replace("%", "%%", false))
        })
    }

    override fun detachComponent() {
        sSkinTypeMap.clear()
    }

    companion object {
        val sSkinTypeMap: MutableMap<String, Skin> = HashMap()
    }
}
