package com.effective.android.service.account.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import java.lang.reflect.Field;

public class EditTextUtil {

    /**
     * 代码设置光标颜色
     *
     * @param editText 你使用的EditText
     * @param cursor    光标颜色
     */
    public static void setCursorDrawableColor(EditText editText, Drawable cursor) {
        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Field fCursorDrawable;
            try {
                Class<?> clazz = editor.getClass();
                fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
                fCursorDrawable.setAccessible(true);
            }catch (Throwable ignore){
                Class<?> clazz = Class.forName("android.widget.Editor");
                fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
                fCursorDrawable.setAccessible(true);
            }
            fCursorDrawable.set(editor, cursor);
        } catch (Throwable ignored) {
        }
    }
}
