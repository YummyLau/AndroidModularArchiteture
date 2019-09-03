package com.effective.android.base.util

import android.text.TextUtils
import java.util.regex.Pattern

object EmojiUtils {

    private const val EMOJI_REGEX = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)"
    private const val EMOJI_LINK_REGEX = "(\u200D)"

    /**
     * 有多少个emoji符号
     */
    fun getEmojiCount(value: String): Int {
        if (TextUtils.isEmpty(value)) {
            return 0
        }
        val pattern = Pattern.compile(EMOJI_REGEX)
        val matcher = pattern.matcher(value)
        var emojiCount = 0
        while (matcher.find()) {
            emojiCount++
        }
        return emojiCount - getEmojiLinkCount(value)

    }

    /**
     * 👪 像这种就是多个emoji连接起来的
     */
    fun getEmojiLinkCount(value: String): Int {
        if (TextUtils.isEmpty(value)) {
            return 0
        }
        val pattern = Pattern.compile(EMOJI_LINK_REGEX)
        val matcher = pattern.matcher(value)
        var emojiLinkCount = 0
        while (matcher.find()) {
            emojiLinkCount++
        }
        return emojiLinkCount
    }


    /**
     * emoji占了多少长度
     */
    fun getEmojiLength(value: String): Int {
        return getEmojiLengthNoLink(value) + getEmojiLinkCount(value)
    }

    /**
     * emoji占了多少长度，不计算emoji连接符
     */
    fun getEmojiLengthNoLink(value: String): Int {
        if (TextUtils.isEmpty(value)) {
            return 0
        }
        var length = 0
        val pattern = Pattern.compile(EMOJI_REGEX)
        val matcher = pattern.matcher(value)
        while (matcher.find()) {
            length += matcher.end() - matcher.start()
        }
        return length
    }


    fun hasEmoji(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            return false
        }
        val pattern = Pattern.compile(EMOJI_REGEX)
        val matcher = pattern.matcher(value)
        return matcher.find()
    }

    /**
     * 返回的int[]可能为空，可能为长度为2的数组，位置0为start，位置1为end
     */
    fun getFirstEmoji(content: String): IntArray? {
        if (TextUtils.isEmpty(content)) {
            return null
        }
        val pattern = Pattern.compile(EMOJI_REGEX)
        val matcher = pattern.matcher(content)
        return if (matcher.find()) {
            intArrayOf(matcher.start(), matcher.end())
        } else {
            null
        }
    }
}