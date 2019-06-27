package example.componentweibo.videmodel;


import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;

import example.basiclib.AbsentLiveData;
import example.basiclib.net.resource.Resource;
import example.weibocomponent.App;
import example.weibocomponent.data.local.db.entity.StatusEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.11.
 */

public class HomeViewModel extends ViewModel {

    private final LiveData<Resource<List<StatusEntity>>> mAllStatus;
    private final MutableLiveData<Boolean> commandRefresh = new MutableLiveData<>();

    public HomeViewModel() {
        ARouter.getInstance().inject(this);
        mAllStatus = Transformations.switchMap(commandRefresh, new Function<Boolean, LiveData<Resource<List<StatusEntity>>>>() {
            @Override
            public LiveData<Resource<List<StatusEntity>>> apply(Boolean toFreshList) {
                if (toFreshList) {
                    return App.demoRepository.getHomeStatus();
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
