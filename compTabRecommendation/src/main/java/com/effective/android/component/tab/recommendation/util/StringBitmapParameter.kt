package com.effective.android.component.tab.recommendation.util


/**
 * https://github.com/Dpuntu/String2Bitmap/blob/master/app/src/main/java/com/fmx/list/string2bitmap/BitmapUtil.java
 * Created by Dpuntu on 2017/3/8.
 */
class StringBitmapParameter {

    companion object{
        const val WIDTH = 384
        const val SMALL_TEXT = 23f
        const val LARGE_TEXT = 35f
        const val START_RIGHT = WIDTH
        const val START_LEFT = 0
        const val START_CENTER = WIDTH / 2

        const  val IS_LARGE = 10
        const val IS_SMALL = 11
        const val IS_RIGHT = 100
        const val IS_LEFT = 101
        const val IS_CENTER = 102
    }

    var text: String
    var isRightOrLeft: Int = 0
    var isSmallOrLarge: Int = 0

    /**
     * @param text 字段
     */
    constructor(text: String) {
        this.text = text
        this.isRightOrLeft = IS_LEFT
        this.isSmallOrLarge = IS_SMALL
    }

    /**
     * @param text          字段
     * @param isRightOrLeft 可空，默认右边
     */
    constructor(text: String, isRightOrLeft: Int) {
        this.text = text
        this.isRightOrLeft = isRightOrLeft
        this.isSmallOrLarge = IS_SMALL
    }

    /**
     * @param text           字段
     * @param isRightOrLeft  可空，默认右边
     * @param isSmallOrLarge 可空，默认小字
     */
    constructor(text: String, isRightOrLeft: Int, isSmallOrLarge: Int) {
        this.text = text
        this.isRightOrLeft = isRightOrLeft
        this.isSmallOrLarge = isSmallOrLarge
    }
}