package com.effective.android.base.view.skin;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.effective.android.base.R;
import com.effective.android.base.util.DisplayUtils;
import com.effective.android.base.util.ResourceUtils;

import skin.support.widget.SkinCompatRelativeLayout;


public class SkinCompatTitleView extends SkinCompatRelativeLayout {

    private TextView title;
    private ImageView left;

    @ColorRes
    private int titleColorId;

    @ColorRes
    private int backgroundColorId;

    @DrawableRes
    private int leftDrawableId;

    public SkinCompatTitleView(Context context) {
        this(context, null);
    }

    public SkinCompatTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.base_title_layout, this, true);
        title = root.findViewById(R.id.title);
        left = root.findViewById(R.id.left);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SkinCompatTitleView, defStyleAttr, 0);
        String titleString = typedArray.getString(R.styleable.SkinCompatTitleView_title);
        if (TextUtils.isEmpty(titleString)) {
            titleString = context.getString(typedArray.getResourceId(R.styleable.SkinCompatTitleView_title, R.string.base_default_title));
        }

        title.setText(titleString);
        titleColorId = typedArray.getResourceId(R.styleable.SkinCompatTitleView_titleColor, R.color.colorThemeText);
        title.setTextColor(ResourceUtils.getColor(context, titleColorId));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.SkinCompatTitleView_titleSize, DisplayUtils.dip2px(context, 20f)));


        left.setVisibility(typedArray.getBoolean(R.styleable.SkinCompatTitleView_titleLeftEnable, false) ? VISIBLE : GONE);
        leftDrawableId = typedArray.getResourceId(R.styleable.SkinCompatTitleView_titleLeftImage, R.drawable.ic_back);
        left.setImageDrawable(ResourceUtils.getDrawable(context, leftDrawableId));

        backgroundColorId = typedArray.getResourceId(R.styleable.SkinCompatTitleView_titleBarBackground, R.color.transparent);
        setBackgroundColor(ResourceUtils.getColor(context, backgroundColorId));
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

    @Override
    public void applySkin() {
        super.applySkin();
        title.setTextColor(ResourceUtils.getColor(getContext(), titleColorId));
        left.setImageDrawable(ResourceUtils.getDrawable(getContext(), leftDrawableId));
        setBackgroundColor(ResourceUtils.getColor(getContext(), backgroundColorId));
    }
}
