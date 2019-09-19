package com.effective.android.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.effective.android.base.R;

public class TitleView extends RelativeLayout {

    private TextView title;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.base_title_layout, this, true);
        title = root.findViewById(R.id.title);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        title.setText(typedArray.getString(R.styleable.TitleView_title));
        setBackgroundColor(typedArray.getColor(R.styleable.TitleView_background, Color.TRANSPARENT));
    }

    public TextView getTitle() {
        return title;
    }
}
