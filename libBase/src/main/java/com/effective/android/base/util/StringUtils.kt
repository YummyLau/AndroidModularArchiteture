package com.effective.android.base.util

import android.text.TextUtils
import java.util.regex.Pattern

object StringUtils {


    private val pattern: Pattern = Pattern.compile("\\s*|\t|\r|\n")

    @JvmStatic
    fun filterSpecialChat(string: String?): String? {
        return if (!TextUtils.isEmpty(string)) {
            pattern.matcher(string).replaceAll("")
        } else {
            string
        }
    }
}