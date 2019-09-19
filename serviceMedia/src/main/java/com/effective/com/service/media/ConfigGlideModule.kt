package com.effective.com.service.media

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule

/**
 * 模拟冲突
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 *
 * registerComponents 方法，可注册所属于所需要处理的组件逻辑，比如加载特定类型的资源
 */
@GlideModule
class ConfigGlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

    }
}
