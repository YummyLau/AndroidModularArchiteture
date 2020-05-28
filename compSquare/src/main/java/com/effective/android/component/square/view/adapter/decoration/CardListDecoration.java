package com.effective.android.component.square.view.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.android.base.util.DisplayUtils;
import com.effective.android.base.util.ResourceUtils;
import com.effective.android.component.square.R;
import com.effective.android.component.square.view.adapter.holder.BannerHolder;

import skin.support.widget.SkinCompatSupportable;


/**
 * 动态列表自定义分割
 * created by yummylau 2019/07/09
 */
public class CardListDecoration extends RecyclerView.ItemDecoration implements SkinCompatSupportable {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    protected Drawable mDivider;
    protected int mOrientation;
    protected int inset;
    protected Paint paint;
    protected RecyclerView recyclerView;
    protected Context context;
    protected Canvas canvas;

    /**
     * @param context
     * @param orientation layout的方向
     */
    public CardListDecoration(Context context, int orientation) {
        this(context, orientation, ResourceUtils.getDrawable(context,R.drawable.square_sh_card_decoration),
                DisplayUtils.dip2px(context,0f), ResourceUtils.getColor(context, R.color.blockBackground));
    }

    public CardListDecoration(Context context, int orientation, Drawable drawable, int inset, int insetColor) {
        mDivider = drawable;
        this.context = context;
        this.inset = inset;
        paint = new Paint();
        paint.setColor(insetColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        setOrientation(orientation);
    }

    @Override
    public void applySkin() {
        mDivider = ResourceUtils.getDrawable(context,R.drawable.square_sh_card_decoration);
        paint.setColor( ResourceUtils.getColor(context, R.color.blockBackground));
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(canvas, recyclerView);
        } else {
            drawHorizontal(canvas, recyclerView);
        }
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    //由于Divider也有宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        this.recyclerView = parent;
        this.canvas = c;
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        //最后一个item不画分割线
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            boolean needFilter = filterSomeHolder(c, parent, true, i, childCount);
            int bottom = top + (needFilter ? 0 : mDivider.getIntrinsicHeight());
            if (needFilter) {
                mDivider.setBounds(0, 0, 0, 0);
                dividerDraw(c, mDivider);
            } else {
                if (inset > 0) {
                    c.drawRect(left, top, right, bottom, paint);
                    mDivider.setBounds(left + inset, top, right - inset, bottom);
                } else {
                    mDivider.setBounds(left, top, right, bottom);
                }
                dividerDraw(c, mDivider);
            }
        }
    }

    private void dividerDraw(Canvas c, Drawable divider) {
        if (!(divider instanceof BitmapDrawable) || !((BitmapDrawable) divider).getBitmap().isRecycled()) {
            divider.draw(c);
        }
    }


    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            boolean needFilter = filterSomeHolder(c, parent, false, i, childCount);
            final int right = left + (needFilter ? 0 : mDivider.getIntrinsicHeight());
            if (needFilter) {
                mDivider.setBounds(0, 0, 0, 0);
                dividerDraw(c, mDivider);
            } else {
                mDivider.setBounds(left, top, right, bottom);
                dividerDraw(c, mDivider);
            }
        }
    }


    private boolean filterSomeHolder(Canvas c, RecyclerView parent, boolean vertical, int index, int size) {
        RecyclerView.ViewHolder currentHolder = parent.getChildViewHolder(parent.getChildAt(index));
        return currentHolder instanceof BannerHolder;
    }
}
