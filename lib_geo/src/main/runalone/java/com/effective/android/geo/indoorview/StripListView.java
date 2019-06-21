/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.effective.android.geo.indoorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 楼层条View
 */
public class StripListView extends ListView {
    public StripListView(Context context) {
        super(context);
        initView(context);
    }

    public StripListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StripListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void setStripAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter.getCount() > 5) {
            View item = adapter.getView(0, null, this);
            item.measure(0, 0);
            layoutParam.height = (int) (5.5 * item.getMeasuredHeight());
            requestLayout();
        }
    }

    RelativeLayout.LayoutParams layoutParam;

    private void initView(Context context) {
        setId(0);
        setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        setVisibility(View.GONE);
        setDividerHeight(0);
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(Color.TRANSPARENT);
        setCacheColorHint(0);
        setSelector(new Drawable() {
            @Override
            public void draw(Canvas canvas) {

            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        });
        layoutParam = new RelativeLayout.LayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParam.setMargins(StripItem.dip2px(context, 20), 0, 0, 0);
        setLayoutParams(layoutParam);
    }
}
