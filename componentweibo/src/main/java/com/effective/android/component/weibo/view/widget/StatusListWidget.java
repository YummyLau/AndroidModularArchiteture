package com.effective.android.component.weibo.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.android.component.weibo.view.adapter.StatusListAdapter;


/**
 * 微博组件，用于暂时微博列表
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class StatusListWidget extends RecyclerView {

    public static final int HOT_TYPE = 0x01;
    public static final int FOLLOW_TYPE = 0x02;

    private int mType;
    private LinearLayoutManager mManager;
    private StatusListAdapter mAdapter;

    public StatusListWidget(Context context) {
        super(context);
    }

    public StatusListWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusListWidget(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initView(int type) {
        mType = type;
        mManager = new LinearLayoutManager(getContext());
        setLayoutManager(mManager);
//        mAdapter = new StatusListAdapter()
    }
}
