package com.effective.android.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.effective.android.base.R;

import java.lang.reflect.Field;


;

/**
 * 状态栏view
 * Created by yummylau on 2017/9/09.
 */

public class StatusBarView extends LinearLayout {

    @ColorRes
    private int mStatusBarColor;

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyle) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.base_statusbar_layout, this, true);
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatusBarView, defStyle, 0);
        if (typedArray != null) {
            mStatusBarColor = typedArray.getResourceId(R.styleable.StatusBarView_status_bar_color, R.color.colorPrimary);
        } else {
            mStatusBarColor = R.color.colorPrimary;
        }
        fitSpecialModelStatusBar();
        setBackgroundColor(ContextCompat.getColor(getContext(), mStatusBarColor));
        int statusbarHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight(getContext()) : 0;
        view.findViewById(R.id.status_bar).setMinimumHeight(statusbarHeight);
//        view.findViewById(R.id.status_bar).getLayoutParams().height = statusbarHeight;
    }


    public static int getStatusBarHeight(Context context) {
        int sbar = 0;
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return sbar;
    }

    /**
     * 特殊机型的状态栏处理.
     */
    private void fitSpecialModelStatusBar() {
        if (TextUtils.equals(Build.MODEL, "vivo X6S A")) {
            mStatusBarColor = R.color.transparent;
        }
    }
}
