package com.effective.android.base.view.dragable.card;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 自定义布局管理器
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private View topView;
    private int touchViewId;

    public CardLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
        this.mRecyclerView = checkIsNull(recyclerView);
        this.mItemTouchHelper = checkIsNull(itemTouchHelper);
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public View getTopView() {
        return topView;
    }

    @Override
    public void onLayoutChildren(final RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        // 当数据源个数大于最大显示数时
        if (itemCount > CardConfig.DEFAULT_SHOW_ITEM) {
            for (int position = CardConfig.DEFAULT_SHOW_ITEM; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (position == CardConfig.DEFAULT_SHOW_ITEM) {
                    view.setScaleX(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY((position - 1) * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                    mOnTouchListener.setUpTouchFromParent(view);
                    topView = view;
                }
            }
        } else {
            // 当数据源个数小于或等于最大显示数时
            for (int position = itemCount - 1; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                    mOnTouchListener.setUpTouchFromParent(view);
                    topView = view;
                }
            }
        }
    }


    private class CardTouchListener implements View.OnTouchListener {

        private TouchFromParent touchFromParent;
        private float lastX = 0;// 记录上次X位置
        private float lastY = 0;// 记录上次Y位置
        boolean startSwipe = false;

        public void setUpTouchFromParent(View view) {
            if (view instanceof TouchFromParent) {
                touchFromParent = (TouchFromParent) view;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchFromParent != null) {
                touchFromParent.onTouch(v, event);
            }
            RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startSwipe = false;
                    lastX = x;
                    lastY = y;
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (startSwipe) {
                        return true;
                    }
                    if (Math.abs(x - lastX) > Math.abs(y - lastY) && !startSwipe) {
                        mItemTouchHelper.startSwipe(childViewHolder);
                        startSwipe = true;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private CardTouchListener mOnTouchListener = new CardTouchListener();

}

