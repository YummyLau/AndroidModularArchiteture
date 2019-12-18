package com.effective.android.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.effective.android.base.R;


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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.base_title_layout, this, true);
        title = root.findViewById(R.id.title);
        left = root.findViewById(R.id.left);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        String titleString = typedArray.getString(R.styleable.TitleView_title);
        if (TextUtils.isEmpty(titleString)) {
            titleString = context.getString(typedArray.getResourceId(R.styleable.TitleView_title, R.string.base_default_title));
        }
        title.setText(titleString);
        title.setTextColor(ContextCompat.getColor(context, typedArray.getResourceId(R.styleable.TitleView_titleColor, R.color.colorThemeText)));
        left.setVisibility(typedArray.getBoolean(R.styleable.TitleView_titleLeftEnable, false) ? VISIBLE : GONE);
        left.setImageDrawable(ContextCompat.getDrawable(context,typedArray.getResourceId(R.styleable.TitleView_titleLeftImage, R.drawable.ic_left)));
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
