/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.effective.android.geo.indoorview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 设置楼层条每个Item 布局
 */
public class StripItem extends FrameLayout {

    public TextView getmText() {
        return mText;
    }

    private TextView mText;
    private static final int ITEM_HEIGHT = 45;
    private static final int ITEM_PADDING = 20;
    public static final int colorSelected = Color.argb(255, 190, 190, 190);
    public static final int color = Color.argb(155, 211, 211, 211);

    public StripItem(Context context) {
        super(context);
        init();
    }

    public StripItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StripItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        LinearLayout layout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(),
                ITEM_HEIGHT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        addView(layout, layoutParams);

        mText = new TextView(getContext());
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setSingleLine();
        mText.setIncludeFontPadding(false);
        mText.setGravity(Gravity.CENTER);
        mText.setTextColor(Color.BLACK);
        mText.setPadding(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING);
        mText.setBackgroundColor(color);
        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.addView(mText, textParams);
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
