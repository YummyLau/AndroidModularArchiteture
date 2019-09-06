package com.effective.android.component.weibo.videmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;

import example.basiclib.AbsentLiveData;
import example.basiclib.net.resource.Resource;

import com.effective.android.component.weibo.data.local.db.entity.UserEntity;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class MainViewModel extends ViewModel {

    private final LiveData<Resource<UserEntity>> ownUserInfo;
    private final MutableLiveData<Boolean> commandInitInfo = new MutableLiveData<>();

    public MainViewModel() {
        ARouter.getInstance().inject(this);
        ownUserInfo = Transformations.switchMap(commandInitInfo, new Function<Boolean, LiveData<Resource<UserEntity>>>() {
            @Override
            public LiveData<Resource<UserEntity>> apply(Boolean commandInitInfo) {
                if (commandInitInfo) {
                    return App.demoRepository.getUserInfo(11l);
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
