package com.effective.android.component.tab.recommendation.util

import android.content.Context
import android.graphics.*
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.IS_CENTER
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.IS_LARGE
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.IS_LEFT
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.IS_RIGHT
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.IS_SMALL
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.LARGE_TEXT
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.SMALL_TEXT
import com.effective.android.component.tab.recommendation.util.StringBitmapParameter.Companion.START_LEFT
import java.util.regex.Pattern
import kotlin.math.abs
import android.graphics.drawable.BitmapDrawable
import kotlinx.android.synthetic.main.tabr_holder_recommend_card_layout.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object StringToBitmapUtils {


    private var x = START_LEFT.toFloat()
    private var y: Float = 0.toFloat()

    @JvmStatic
    fun createBitmapWithText(context: Context,bitmapDrawable: BitmapDrawable,text:String,fontSize: Int,textBitmapModel:Boolean = true): Bitmap? {
        val backColor: Int = Color.parseColor("#FFFFFF")
        val bitmap = (bitmapDrawable).bitmap
        var target: Bitmap?
        target = if (textBitmapModel) {
            getTextBitmap(bitmap, backColor, text, fontSize)
        } else {
            getBlockBitmap(bitmap, fontSize)
        }
        var fileOutputStream: FileOutputStream? = null
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        try {
            val file = File(context.cacheDir, "cardDir")
            if (!file.exists()) {
                file.createNewFile()
            }
            fileOutputStream = FileOutputStream(file)
            byteArrayOutputStream = ByteArrayOutputStream()
            target!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val data = byteArrayOutputStream!!.toByteArray()
            fileOutputStream!!.write(data, 0, data.size)
            fileOutputStream!!.flush()
            return BitmapFactory.decodeByteArray(data, 0, data.size)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    /**
     * 生成图片
     */
    @JvmStatic
    fun StringListToBitmap(context: Context, allString: MutableList<StringBitmapParameter>): Bitmap {
        if (allString.isNullOrEmpty()) {
            return Bitmap.createBitmap(StringBitmapParameter.WIDTH, StringBitmapParameter.WIDTH / 4, Bitmap.Config.RGB_565)
        }

        val mBreakString = mutableListOf<StringBitmapParameter>()

        val paint = Paint()
        paint.isAntiAlias = false
        paint.textSize = StringBitmapParameter.SMALL_TEXT

        val typeface = Typeface.createFromAsset(context.assets, "fonts/songti.TTF")// 仿宋打不出汉字
        val font = Typeface.create(typeface, Typeface.NORMAL)
        paint.typeface = font

        for (mParameter in allString) {
            val aLineLength = paint.breakText(mParameter.text, true, StringBitmapParameter.WIDTH.toFloat(), null)//检测一行多少字
            val length = mParameter.text.length
            if (aLineLength < length) {
                val num = length / aLineLength
                var aLineString: String?
                var remainString: String?
                for (j in 0 until num) {
                    aLineString = mParameter.text.substring(j * aLineLength, (j + 1) * aLineLength)
                    mBreakString.add(StringBitmapParameter(aLineString, mParameter.isRightOrLeft, mParameter.isSmallOrLarge))
                }

                remainString = mParameter.text.substring(num * aLineLength, mParameter.text.length)
                mBreakString.add(StringBitmapParameter(remainString, mParameter.isRightOrLeft, mParameter.isSmallOrLarge))
            } else {
                mBreakString.add(mParameter)
            }
        }

        val fontMetrics = paint.fontMetrics
        val fontHeight = abs(fontMetrics.leading).toInt() + abs(fontMetrics.ascent).toInt() + abs(fontMetrics.descent).toInt()
        y = abs(fontMetrics.leading) + abs(fontMetrics.ascent)

        var bNum = 0
        for (mParameter in mBreakString) {
            val bStr = mParameter.text
            if (bStr.isEmpty() or bStr.contains("\n") or (mParameter.isSmallOrLarge === IS_LARGE))
                bNum++
        }
        val bitmap = Bitmap.createBitmap(StringBitmapParameter.WIDTH, fontHeight * (mBreakString.size + bNum), Bitmap.Config.RGB_565)

        drawWhiteBackground(bitmap)

        val canvas = Canvas(bitmap)

        for (mParameter in mBreakString) {

            val str = mParameter.text

            if (mParameter.isSmallOrLarge === IS_SMALL) {
                paint.textSize = SMALL_TEXT
            } else if (mParameter.isSmallOrLarge === IS_LARGE) {
                paint.textSize = LARGE_TEXT
            }

            when {
                mParameter.isRightOrLeft === IS_RIGHT -> x = StringBitmapParameter.WIDTH - paint.measureText(str)
                mParameter.isRightOrLeft === IS_LEFT -> x = START_LEFT.toFloat()
                mParameter.isRightOrLeft === IS_CENTER -> x = (StringBitmapParameter.WIDTH - paint.measureText(str)) / 2.0f
            }

            if (str.isEmpty() or str.contains("\n") or (mParameter.isSmallOrLarge === IS_LARGE)) {
                canvas.drawText(str, x, y + fontHeight / 2, paint)
                y += fontHeight
            } else {
                canvas.drawText(str, x, y, paint)
            }
            y += fontHeight
        }
        canvas.save()
        canvas.restore()
        return bitmap
    }

    /**
     * 合并图片
     */
    @JvmStatic
    fun addBitmapInHead(first: Bitmap, second: Bitmap): Bitmap {
        val width = Math.max(first.width, second.width)
        val startWidth = (width - first.width) / 2
        val height = first.height + second.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        drawWhiteBackground(result)

        val canvas = Canvas(result)
        canvas.drawBitmap(first, startWidth.toFloat(), 0f, null)
        canvas.drawBitmap(second, 0f, first.height.toFloat(), null)
        return result
    }

    /***
     * 使用两个方法的原因是：
     * logo标志需要居中显示，如果直接使用同一个方法是可以显示的，但是不会居中
     */
    @JvmStatic
    fun addBitmapInFoot(bitmap: Bitmap, image: Bitmap): Bitmap {
        val width = Math.max(bitmap.width, image.width)
        val startWidth = (width - image.width) / 2
        val height = bitmap.height + image.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        drawWhiteBackground(result)

        val canvas = Canvas(result)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawBitmap(image, startWidth.toFloat(), bitmap.height.toFloat(), null)
        return result
    }

    private fun drawWhiteBackground(bitmap: Bitmap) {
        val width = bitmap.width
        val height = bitmap.height
        val length = width * height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in 0 until length) {
            pixels[i] = Color.rgb(255, 255, 255)
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }


    /**
     * 文本转成图片
     * @param bitmap 原图片
     * @param text 文本
     * @param fontSize 文字大小
     * @return
     */
    fun getTextBitmap(bitmap: Bitmap?, backColor: Int, text: String, fontSize: Int): Bitmap {
        requireNotNull(bitmap) { "Bitmap cannot be null." }
        val picWidth = bitmap.width
        val picHeight = bitmap.height
        val back = Bitmap.createBitmap(if (bitmap.width % fontSize == 0) bitmap.width else bitmap.width / fontSize * fontSize, if (bitmap.height % fontSize == 0) bitmap.height else bitmap.height / fontSize * fontSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(back)
        canvas.drawColor(backColor)
        var idx = 0
        run {
            var y = 0
            while (y < picHeight) {
                run {
                    var x = 0
                    while (x < picWidth) {
                        val colors = getPixels(bitmap, x, y, fontSize, fontSize)

                        val paint = Paint()
                        paint.isAntiAlias = true
                        paint.color = getAverage(colors)
                        paint.textSize = fontSize.toFloat()
                        val fontMetrics = paint.fontMetrics
                        val padding = if (y == 0) fontSize + fontMetrics.ascent else (fontSize + fontMetrics.ascent) * 2
                        canvas.drawText(text[idx++].toString(), x.toFloat(), y - padding, paint)
                        if (idx == text.length) {
                            idx = 0
                        }
                        x += fontSize

                    }
                }
                y += fontSize
            }
        }

        return back
    }

    /**
     * 转成像素图片
     * @param bitmap 原图片
     * @param blockSize 块大小
     * @return
     */
    fun getBlockBitmap(bitmap: Bitmap?, blockSize: Int): Bitmap {
        requireNotNull(bitmap) { "Bitmap cannot be null." }
        val picWidth = bitmap.width
        val picHeight = bitmap.height
        val back = Bitmap.createBitmap(if (bitmap.width % blockSize == 0) bitmap.width else bitmap.width / blockSize * blockSize, if (bitmap.height % blockSize == 0) bitmap.height else bitmap.height / blockSize * blockSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(back)
        canvas.drawColor(0xfff)
        run {
            var y = 0
            while (y < picHeight) {
                run {
                    var x = 0
                    while (x < picWidth) {
                        val colors = getPixels(bitmap, x, y, blockSize, blockSize)

                        val paint = Paint()
                        paint.isAntiAlias = true
                        paint.color = getAverage(colors)
                        paint.style = Paint.Style.FILL
                        val left = x
                        val top = y
                        val right = x + blockSize
                        val bottom = y + blockSize

                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
                        x += blockSize

                    }
                }
                y += blockSize
            }
        }

        return back
    }

    /**
     * 获取某一块的所有像素的颜色
     * @param bitmap
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    private fun getPixels(bitmap: Bitmap, x: Int, y: Int, w: Int, h: Int): IntArray {
        val colors = IntArray(w * h)
        var idx = 0
        var i = y
        while (i < h + y && i < bitmap.height) {
            var j = x
            while (j < w + x && j < bitmap.width) {
                val color = bitmap.getPixel(j, i)
                colors[idx++] = color
                j++
            }
            i++
        }
        return colors
    }

    /**
     * 求取多个颜色的平均值
     * @param colors
     * @return
     */
    private fun getAverage(colors: IntArray): Int {
        //int alpha=0;
        var red = 0
        var green = 0
        var blue = 0
        for (color in colors) {
            red += color and 0xff0000 shr 16
            green += color and 0xff00 shr 8
            blue += color and 0x0000ff
        }
        val len = colors.size.toFloat()
        //alpha=Math.round(alpha/len);
        red = Math.round(red / len)
        green = Math.round(green / len)
        blue = Math.round(blue / len)

        return Color.argb(0xff, red, green, blue)
    }

    private fun log(log: String) {
        println("-------->Utils:$log")
    }
}