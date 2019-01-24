package example.weibocomponent.videmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;

import javax.inject.Inject;

import example.basiclib.AbsentLiveData;
import example.basiclib.net.resource.Resource;
import example.weibocomponent.data.DemoRepository;
import example.weibocomponent.data.local.db.entity.StatusEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.11.
 */

public class HomeViewModel extends ViewModel {

    @Inject
    public DemoRepository featureRepository;

    private final LiveData<Resource<List<StatusEntity>>> mAllStatus;
    private final MutableLiveData<Boolean> commandRefresh = new MutableLiveData<>();

    @Inject
    public HomeViewModel() {
        ARouter.getInstance().inject(this);
        mAllStatus = Transformations.switchMap(commandRefresh, new Function<Boolean, LiveData<Resource<List<StatusEntity>>>>() {
            @Override
            public LiveData<Resource<List<StatusEntity>>> apply(Boolean toFreshList) {
                if (toFreshList) {
                    return featureRepository.getHomeStatus();
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
