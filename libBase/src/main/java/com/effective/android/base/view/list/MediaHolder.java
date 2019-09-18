package com.effective.android.base.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class MediaHolder<T extends IMediaItem> extends RecyclerView.ViewHolder {

    public Context context;
    public MediaAdapter adapter;

    public MediaHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public MediaHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    protected void bindData(MediaAdapter adapter, T data, int position, @NonNull List<Object> payloads) {
        this.adapter = adapter;
        bindData(data, position, payloads);
    }

    public abstract void bindData(T data, int position, @NonNull List<Object> payloads);
}
