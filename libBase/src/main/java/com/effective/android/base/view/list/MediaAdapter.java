package com.effective.android.base.view.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaAdapter<T extends IMediaItem> extends RecyclerView.Adapter<MediaHolder<T>> {

    public List<T> dataList;

    public MediaAdapter(List<T> dataList) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder<T> holder, int position) {
        holder.bindData(this, dataList.get(position), position, null);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder<T> holder, int position, @NonNull List<Object> payloads) {
        holder.bindData(this, dataList.get(position), position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
