package com.effective.android.base.view.corner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.effective.android.base.R;
import com.effective.android.base.util.DisplayUtils;
import com.effective.android.base.util.ResourceUtils;


/**
 * 用于画圆角
 * created bg g8931
 */
public class CornerView extends View{

    private Paint paint;
    private int cornerDirection;
    private int size;
    private boolean checkoutReport;
    private int cusFgColor = -1;

    public CornerView(Context context) {
        this(context, null);
    }

    public CornerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CornerView);
            if (array != null) {
                cornerDirection = array.getInt(R.styleable.CornerView_corner_direction, 1);
                size = array.getDimensionPixelSize(R.styleable.CornerView_corner_size, 0);
                cusFgColor = array.getResourceId(R.styleable.CornerView_corner_color, -1);
                array.recycle();
            }
        }
        if (size == 0) {
            size = DisplayUtils.dip2px(getContext(), 10f);
        }
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(size, size);
    }

    public void setRepost(boolean checkoutReport) {
        this.checkoutReport = checkoutReport;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setLayerType(LAYER_TYPE_HARDWARE, null);
        canvas.drawARGB(0, 0, 0, 0);

        Bitmap fgBitmap = getFgBitmap();
        Bitmap bgBitmap = getBgBitmap();

        canvas.drawBitmap(fgBitmap, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bgBitmap, 0, 0, paint);

        paint.setXfermode(null);
        fgBitmap.recycle();
        bgBitmap.recycle();
    }


    protected @ColorRes
    int getFgColor() {
        if (cusFgColor != -1) {
            return cusFgColor;
        }
        return R.color.windowBackground;
    }

    @NonNull
    private Bitmap getFgBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ResourceUtils.getColor(getContext(), getFgColor()));

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRect(0, 0, size, size, paint);
        return bitmap;
    }

    @NonNull
    private Bitmap getBgBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        canvas.drawARGB(0, 0, 0, 0);
        switch (cornerDirection) {
            case 1: {
                canvas.drawCircle(size, size, size, paint);
                break;
            }
            case 2: {
                canvas.drawCircle(0, size, size, paint);
                break;
            }
            case 3: {
                canvas.drawCircle(size, 0, size, paint);
                break;
            }
            case 4: {
                canvas.drawCircle(0, 0, size, paint);
                break;
            }
        }
        return bitmap;
    }
}
