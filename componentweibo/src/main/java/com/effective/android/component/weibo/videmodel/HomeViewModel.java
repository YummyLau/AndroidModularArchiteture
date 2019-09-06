package com.effective.android.component.weibo.videmodel;


import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import java.util.List;

import com.effective.android.component.weibo.AbsentLiveData;
import com.effective.android.component.weibo.WeiboComponent;
import com.effective.android.component.weibo.data.local.db.entity.StatusEntity;
import com.effective.android.component.weibo.net.resource.Resource;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.11.
 */

public class HomeViewModel extends ViewModel {

    private final LiveData<Resource<List<StatusEntity>>> mAllStatus;
    private final MutableLiveData<Boolean> commandRefresh = new MutableLiveData<>();

    public HomeViewModel() {
        mAllStatus = Transformations.switchMap(commandRefresh, new Function<Boolean, LiveData<Resource<List<StatusEntity>>>>() {
            @Override
            public LiveData<Resource<List<StatusEntity>>> apply(Boolean toFreshList) {
                if (toFreshList) {
                    return WeiboComponent.demoRepository.getHomeStatus();
                } else {
                    return AbsentLiveData.create();
                }
            }
        });
    }

    public LiveData<Resource<List<StatusEntity>>> getAllStatus() {
        return mAllStatus;
    }

    public void refresh() {
        commandRefresh.setValue(true);
    }
}
