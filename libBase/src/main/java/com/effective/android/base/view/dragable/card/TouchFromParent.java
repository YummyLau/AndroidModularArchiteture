package com.effective.android.base.view.dragable.card;

import android.view.MotionEvent;
import android.view.View;

public interface TouchFromParent {
    void onTouch(View v, MotionEvent event);
}
