package com.effective.android.component.tab.mine.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.effective.android.base.util.DisplayUtils;


public class PrivilegeProgressBar extends ProgressBar {
    private Paint mPaint;
    private String progressText = "0";
    private float textX;
    private float textY;

    public PrivilegeProgressBar(Context context) {
        super(context);
        init();
    }

    public PrivilegeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DisplayUtils.dip2px(getContext(), 10));
        setMax(100);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawText(progressText, textX, textY, mPaint);
        canvas.restore();
    }

    public void setPrivilegeProgress(int progress) {
        progress = progress % 100;
        String mText = progress + "";
        boolean textChange = !TextUtils.equals(progressText, mText);
        this.progressText = mText;
        Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), rect);
        textY = (getHeight() / 2) - rect.centerY();


        float radio = 1f;
        if (progress < getMax()) {
            radio = progress * 1.0f / getMax();
        }
        float progressPosX = getWidth() * radio;
        int paddingLeft = DisplayUtils.dip2px(getContext(), 16);
        int paddingRight = DisplayUtils.dip2px(getContext(), 6);
        float textWidth = mPaint.measureText(mText);
        //水平居中
        textX = Math.max((progressPosX - paddingLeft - paddingRight - textWidth) / 2 + paddingLeft, paddingLeft);
        progress = (int) (Math.max(progressPosX, (textX + textWidth + paddingRight)) * getMax() / getWidth());
        if (progress >= getMax()) {
            progress = getMax();
        }
        if (textChange && getProgress() == progress) {
            invalidate();
        }
        setProgress(progress);
    }
}
