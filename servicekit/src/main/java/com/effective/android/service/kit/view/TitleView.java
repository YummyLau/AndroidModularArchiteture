package com.effective.android.service.kit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.effective.android.service.kit.R;


public class TitleView extends RelativeLayout {

    private TextView title;
    private ImageView left;

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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.kit_title_layout, this, true);
        title = root.findViewById(R.id.title);
        left = root.findViewById(R.id.left);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        title.setText(typedArray.getString(R.styleable.TitleView_title));
        title.setTextColor(ContextCompat.getColor(context, typedArray.getResourceId(R.styleable.TitleView_titleColor, R.color.colorThemeText)));
        setBackgroundColor(ContextCompat.getColor(context, typedArray.getResourceId(R.styleable.TitleView_titleBarBackground, R.color.transparent)));
    }

    public ImageView getLeftAction() {
        return left;
    }
    public void setTitle(String title) {
        this.title.setText(title);
    }

    public TextView getTitle() {
        return title;
    }
}
