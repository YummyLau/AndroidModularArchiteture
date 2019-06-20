package com.effective.android.base.util

import android.text.TextUtils
import java.util.regex.Pattern

object CharUtils {

    private val PATTERN = Pattern.compile("[\u4e00-\u9fa5]") // 中文
    private val NOT_CHINESE_NUM_LETTER_PATTERN = Pattern.compile("[^\\u4e00-\\u9fa5a-zA-Z0-9]+")   // 非中文、字母、数字


    /**
     * 中文、中文标点算2个字符，字母和数字为1个字符
     *
     * @param value 需要计算的文字内容
     * @return 文字长度
     */
    fun getLength(value: String): Int {
        if (TextUtils.isEmpty(value)) {
            return 0
        }
        val matcher = PATTERN.matcher(value)
        var chineseCount = 0
        while (matcher.find()) {
            chineseCount++
        }
        var chinesePunctuationCount = 0 // 中文标点
        for (i in 0 until value.length) {
            val singleChar = value[i]
            if (isChinesePunctuation(singleChar)) {
                chinesePunctuationCount++
            }
        }
        val allCount = value.length
        val otherCount = allCount - chineseCount - chinesePunctuationCount
        return chineseCount * 2 + chinesePunctuationCount * 2 + otherCount
    }

    fun getLength(value: CharSequence): Int {
        return if (TextUtils.isEmpty(value)) {
            0
        } else getLength(value.toString())
    }

    /**
     * 根据UnicodeBlock方法判断中文标点符号
     */
    fun isChinesePunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS)
    }

    fun isChinese(c: Char): Boolean {
        try {
            val string = Character.toString(c)
            if (TextUtils.isEmpty(string)) {
                return false
            }
            val matcher = PATTERN.matcher(string)
            return matcher.find()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun getChinese(text: String): String {
        if (TextUtils.isEmpty(text)) {
            return ""
        }
        val matcher = PATTERN.matcher(text)
        val stringBuffer = StringBuffer("")
        while (matcher.find()) {
            stringBuffer.append(matcher.group())
        }
        return stringBuffer.toString()
    }

    /**
     * 是否为英文标点符号
     */
    fun isEnglishPuncPunctuation(ch: Char): Boolean {
        if (0x21 <= ch.toInt() && ch.toInt() <= 0x22) return true
        if (ch.toInt() == 0x27 || ch.toInt() == 0x2C) return true
        if (ch.toInt() == 0x2E || ch.toInt() == 0x3A) return true
        return if (ch.toInt() == 0x3B || ch.toInt() == 0x3F) true else false
    }

    /**
     * 是否为emoji
     */
    fun isEmojiCharacter(codePoint: Char): Boolean {
        return !(codePoint.toInt() == 0x0 ||
                codePoint.toInt() == 0x9 ||
                codePoint.toInt() == 0xA ||
                codePoint.toInt() == 0xD ||
                codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0xD7FF ||
                codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFFFD ||
                codePoint.toInt() >= 0x10000 && codePoint.toInt() <= 0x10FFFF)
    }

    /**
     * 是否非中文、英文、数字
     */
    private fun isNotChineseAndNumAndLetter(ch: Char): Boolean {
        val matcher = NOT_CHINESE_NUM_LETTER_PATTERN.matcher(ch.toString())
        return matcher.find()
    }

    /**
     * 是否为特殊符号（非中文、非数字、非英文标点、非中文标点、非字母、非emoji）
     * @return
     */
    private fun isSpecialChar(ch: Char): Boolean {
        return (!isChinesePunctuation(ch)
                && !isEnglishPuncPunctuation(ch)
                && !isEmojiCharacter(ch)
                && isNotChineseAndNumAndLetter(ch))
    }

    /**
     * 获取有emoji的时候的长度，中文及中文符号占两个，英文及emoji还有其他占一个
     */
    fun getLengthWithEmoji(value: String): Int {
        return getLength(value) + EmojiUtils.getEmojiCount(value) - EmojiUtils.getEmojiLength(value) - EmojiUtils.getEmojiLinkCount(value)
    }

}