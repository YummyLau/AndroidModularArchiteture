package example.weibocomponent.view.holder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by g8931 on 2017/12/5.
 */

public abstract class BaseHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected T binding;

    public BaseHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    protected abstract void bindData(int position);
}
