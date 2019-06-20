package com.effective.android.base.util

import android.os.Build
import android.text.TextUtils
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object RomUtil {

    val ROM_MIUI = "MIUI"
    val ROM_EMUI = "EMUI"
    val ROM_FLYME = "FLYME"
    val ROM_OPPO = "OPPO"
    val ROM_SMARTISAN = "SMARTISAN"
    val ROM_VIVO = "VIVO"
    val ROM_QIKU = "QIKU"

    private val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private val KEY_VERSION_EMUI = "ro.build.version.emui"
    private val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private val KEY_VERSION_VIVO = "ro.vivo.os.version"


    private val TAG = RomUtil::class.java.simpleName

    private var sName: String? = null
    private var sVersion: String? = null

    val isEmui: Boolean
        get() = check(ROM_EMUI)

    val isMiui: Boolean
        get() = check(ROM_MIUI)

    val isVivo: Boolean
        get() = check(ROM_VIVO)

    val isOppo: Boolean
        get() = check(ROM_OPPO)

    val isFlyme: Boolean
        get() = check(ROM_FLYME)

    val is360: Boolean
        get() = check(ROM_QIKU) || check("360")

    val isSmartisan: Boolean
        get() = check(ROM_SMARTISAN)

    val name: String?
        get() {
            if (sName == null) {
                check("")
            }
            return sName
        }

    val version: String?
        get() {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

    fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }

        sVersion = getProp(KEY_VERSION_MIUI)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_MIUI
            return sName == sVersion
        }

        sVersion = getProp(KEY_VERSION_EMUI)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_EMUI
            return sName == sVersion
        }

        sVersion = getProp(KEY_VERSION_OPPO)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_OPPO
            return sName == sVersion
        }

        sVersion = getProp(KEY_VERSION_VIVO)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_VIVO
            return sName == sVersion
        }

        sVersion = getProp(KEY_VERSION_SMARTISAN)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_SMARTISAN
            return sName == sVersion
        }

        sVersion = Build.DISPLAY
        if (sVersion!!.toUpperCase().contains(ROM_FLYME)) {
            sName = ROM_FLYME
        } else {
            sVersion = Build.UNKNOWN
            sName = Build.MANUFACTURER.toUpperCase()
        }
        return sName == rom
    }


    fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Log.e(TAG, "Unable to read prop $name", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return line
    }
}
