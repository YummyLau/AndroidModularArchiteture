package com.debug.library.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 显示加载进度
 * Created by yummyLau on 18-09-17
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class ProgressView extends ProgressBar {

    private Paint mPaint;
    private int mWidth, mHeight;
    private int progress;//加载进度

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, ow, oh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth * progress / 100, mHeight, mPaint);
        super.onDraw(canvas);
    }

    /**
     * 设置新进度 重新绘制
     *
     * @param newProgress 新进度
     */
    public void setProgress(int newProgress) {
        this.progress = newProgress;
        invalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param color 色值
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }
}
