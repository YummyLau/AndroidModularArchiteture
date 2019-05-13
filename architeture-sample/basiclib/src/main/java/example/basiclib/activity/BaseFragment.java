package example.basiclib.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public abstract class BaseFragment<VM extends ViewModel, DB extends ViewDataBinding> extends Fragment {

    ViewModelProvider.Factory viewModelFactory;

    public VM viewModel;

    public DB dataBinding;

    public abstract Class<VM> getViewModel();

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(getViewModel());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return dataBinding.getRoot();
    }
}
