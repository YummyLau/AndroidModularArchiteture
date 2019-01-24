package example.weibocomponent.videmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;

import javax.inject.Inject;

import example.basiclib.AbsentLiveData;
import example.basiclib.net.resource.Resource;
import example.weibocomponent.data.DemoRepository;
import example.weibocomponent.data.local.db.entity.UserEntity;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class MainViewModel extends ViewModel {

    @Inject
    public DemoRepository featureRepository;

    private final LiveData<Resource<UserEntity>> ownUserInfo;
    private final MutableLiveData<Boolean> commandInitInfo = new MutableLiveData<>();

    @Inject
    public MainViewModel() {
        ARouter.getInstance().inject(this);
        ownUserInfo = Transformations.switchMap(commandInitInfo, new android.arch.core.util.Function<Boolean, LiveData<Resource<UserEntity>>>() {
            @Override
            public LiveData<Resource<UserEntity>> apply(Boolean commandInitInfo) {
                if (commandInitInfo) {
                    return featureRepository.getUserInfo(11l);
                } else {
                    return AbsentLiveData.create();
                }
            }
        });
    }

    public LiveData<Resource<UserEntity>> getUser() {
        return ownUserInfo;
    }

    public void initInfo() {
        commandInitInfo.setValue(true);
    }

}
