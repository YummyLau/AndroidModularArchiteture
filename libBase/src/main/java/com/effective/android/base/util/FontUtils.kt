package com.effective.android.base.util

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.lang.ref.SoftReference
import java.util.HashMap

object FontUtils {

    private val mCache = HashMap<String, SoftReference<Typeface>>()

    /**
     *
     * Replace the font of specified view and it's children
     *
     * @param root     The root view.
     * @param fontPath font file path relative to 'assets' directory.
     */
    fun replaceFontFromAsset(root: View, fontPath: String) {
        replaceFont(root, createTypefaceFromAsset(root.context, fontPath))
    }

    /**
     *
     * Replace the font of specified view and it's children
     *
     * @param root     The root view.
     * @param fontPath font file path relative to 'assets' directory.
     * @param style    One of [Typeface.NORMAL], [Typeface.BOLD], [Typeface.ITALIC], [Typeface.BOLD_ITALIC]
     */
    fun replaceFontFromAsset(root: View, fontPath: String, style: Int) {
        replaceFont(root, createTypefaceFromAsset(root.context, fontPath), style)
    }

    /**
     *
     * Replace the font of specified view and it's children
     *
     * @param root     The root view.
     * @param fontPath The full path to the font data.
     */
    fun replaceFontFromFile(root: View, fontPath: String) {
        replaceFont(root, createTypefaceFromFile(fontPath))
    }

    /**
     *
     * Replace the font of specified view and it's children
     *
     * @param root     The root view.
     * @param fontPath The full path to the font data.
     * @param style    One of [Typeface.NORMAL], [Typeface.BOLD], [Typeface.ITALIC], [Typeface.BOLD_ITALIC]
     */
    fun replaceFontFromFile(root: View, fontPath: String, style: Int) {
        replaceFont(root, createTypefaceFromFile(fontPath), style)
    }

    /**
     *
     * Replace the font of specified view and it's children with specified typeface
     */
    private fun replaceFont(root: View?, typeface: Typeface?) {
        if (root == null || typeface == null) {
            return
        }

        if (root is TextView) { // If view is TextView or it's subclass, replace it's font
            val textView = root as TextView?
            // Extract previous style of TextView
            var style = Typeface.NORMAL
            if (textView!!.typeface != null) {
                style = textView.typeface.style
            }
            textView.setTypeface(typeface, style)
        } else if (root is ViewGroup) { // If view is ViewGroup, apply this method on it's child views
            val viewGroup = root as ViewGroup?
            for (i in 0 until viewGroup!!.childCount) {
                replaceFont(viewGroup.getChildAt(i), typeface)
            }
        } // else return
    }

    /**
     *
     * Replace the font of specified view and it's children with specified typeface and text style
     *
     * @param style One of [Typeface.NORMAL], [Typeface.BOLD], [Typeface.ITALIC], [Typeface.BOLD_ITALIC]
     */
    private fun replaceFont(root: View?, typeface: Typeface?, style: Int) {
        var style = style
        if (root == null || typeface == null) {
            return
        }
        if (style < 0 || style > 3) {
            style = Typeface.NORMAL
        }

        if (root is TextView) { // If view is TextView or it's subclass, replace it's font
            val textView = root as TextView?
            textView!!.setTypeface(typeface, style)
        } else if (root is ViewGroup) { // If view is ViewGroup, apply this method on it's child views
            val viewGroup = root as ViewGroup?
            for (i in 0 until viewGroup!!.childCount) {
                replaceFont(viewGroup.getChildAt(i), typeface, style)
            }
        } // else return
    }

    /**
     *
     * Create a Typeface instance with specified font file
     *
     * @param fontPath font file path relative to 'assets' directory.
     * @return Return created typeface instance.
     */
    private fun createTypefaceFromAsset(context: Context, fontPath: String): Typeface? {
        var typefaceRef = mCache[fontPath]
        var typeface: Typeface? = null
        if (typefaceRef != null) {
            typeface = typefaceRef.get()
            if (typeface != null) {
                return typeface
            }
        }
        typeface = Typeface.createFromAsset(context.assets, fontPath)
        typefaceRef = SoftReference(typeface)
        mCache[fontPath] = typefaceRef
        return typeface
    }

    private fun createTypefaceFromFile(fontPath: String): Typeface? {
        var typefaceRef = mCache[fontPath]
        var typeface: Typeface? = null
        if (typefaceRef != null) {
            typeface = typefaceRef.get()
            if (typeface != null) {
                return typeface
            }
        }
        typeface = Typeface.createFromFile(fontPath)
        typefaceRef = SoftReference(typeface)
        mCache[fontPath] = typefaceRef
        return typeface
    }

    /**
     *
     * Replace system default font. **Note:**you should also add code below to your app theme in styles.xml.
     * `<item name="android:typeface">monospace</item>`
     *
     * The best place to call this method is [Application.onCreate], it will affect
     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.
     *
     * @param context  [Context]
     * @param fontPath font file path relative to 'assets' directory.
     */
    fun replaceSystemDefaultFontFromAsset(context: Context, fontPath: String) {
        replaceSystemDefaultFont(createTypefaceFromAsset(context, fontPath))
    }

    /**
     *
     * Replace system default font. **Note:**you should also add code below to your app theme in styles.xml.
     * `<item name="android:typeface">monospace</item>`
     *
     * The best place to call this method is [Application.onCreate], it will affect
     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.
     *
     * @param context  [Context]
     * @param fontPath The full path to the font data.
     */
    fun replaceSystemDefaultFontFromFile(context: Context, fontPath: String) {
        replaceSystemDefaultFont(createTypefaceFromFile(fontPath))
    }

    /**
     *
     * Replace system default font. **Note:**you should also add code below to your app theme in styles.xml.
     * `<item name="android:typeface">monospace</item>`
     *
     * The best place to call this method is [Application.onCreate], it will affect
     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.
     */
    private fun replaceSystemDefaultFont(typeface: Typeface?) {
        modifyObjectField(null, "MONOSPACE", typeface)
    }

    private fun modifyObjectField(obj: Any?, fieldName: String, value: Any?) {
        try {
            val defaultField = Typeface::class.java.getDeclaredField(fieldName)
            defaultField.isAccessible = true
            defaultField.set(obj, value)

        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}
