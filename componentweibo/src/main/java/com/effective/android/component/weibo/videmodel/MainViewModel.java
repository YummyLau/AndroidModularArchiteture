package com.effective.android.component.weibo.videmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.effective.android.component.weibo.AbsentLiveData;
import com.effective.android.component.weibo.WeiboComponent;
import com.effective.android.component.weibo.data.local.db.entity.UserEntity;
import com.effective.android.component.weibo.net.resource.Resource;
import com.effective.android.service.account.AccountResult;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class MainViewModel extends ViewModel {

    private final LiveData<Resource<UserEntity>> ownUserInfo;
    private final MutableLiveData<Boolean> commandInitInfo = new MutableLiveData<>();

    public MainViewModel() {
        ownUserInfo = Transformations.switchMap(commandInitInfo, new Function<Boolean, LiveData<Resource<UserEntity>>>() {
            @Override
            public LiveData<Resource<UserEntity>> apply(Boolean commandInitInfo) {
                if (commandInitInfo) {
                    return WeiboComponent.demoRepository.getUserInfo(11L);
                } else {
                    return AbsentLiveData.create();
                }
            }
        });
    }


    public void loadUserInfo(){
        if(WeiboComponent.accountSdk.isLogin()){

        }
    }

    public LiveData<Resource<UserEntity>> getUser() {
        return ownUserInfo;
    }

    public void initInfo() {
        commandInitInfo.setValue(true);
    }

    public boolean isLogin() {
        return WeiboComponent.accountSdk.isLogin();
    }

}
