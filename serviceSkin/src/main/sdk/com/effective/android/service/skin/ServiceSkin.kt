package com.effective.android.service.skin


interface ServiceSkin {

    fun getSkins(): List<Skin>

    fun getCurSkin(): Skin

    fun isLoadingDefaultSkin(): Boolean

    fun changeSkin()

    fun changeSkin(skin: Skin)

    fun addSkinChangeListener(skinChangeListener: SkinChangeListener?)

    fun removeSkinChangeListener(skinChangeListener: SkinChangeListener?)
}
