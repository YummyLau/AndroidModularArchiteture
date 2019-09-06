package com.effective.android.component.weibo.view.holder;


import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public abstract class BaseHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected T binding;

    public BaseHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    protected abstract void bindData(int position);
}
