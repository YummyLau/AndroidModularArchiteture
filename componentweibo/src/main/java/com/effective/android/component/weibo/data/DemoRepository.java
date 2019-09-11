package com.effective.android.component.weibo.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.Callable;

import com.effective.android.base.rxjava.Rx2Creator;
import com.effective.android.component.weibo.WeiboComponent;
import com.effective.android.component.weibo.data.local.db.AppDataBase;
import com.effective.android.component.weibo.data.local.db.entity.StatusEntity;
import com.effective.android.component.weibo.data.local.db.entity.UserEntity;
import com.effective.android.component.weibo.data.remote.api.WeiboApis;
import com.effective.android.component.weibo.data.remote.result.StatusResult;
import com.effective.android.component.weibo.net.resource.NetworkBoundResource;
import com.effective.android.component.weibo.net.resource.Resource;
import com.effective.android.service.account.Account;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public class DemoRepository implements DemoDataSource {

    private AppDataBase mAppDataBase;
    private WeiboApis mWeiboApis;

    public DemoRepository(AppDataBase appDataBase, WeiboApis weiboApis) {
        this.mAppDataBase = appDataBase;
        this.mWeiboApis = weiboApis;
    }


    @Override
    public LiveData<Resource<List<StatusEntity>>> getHomeStatus() {
        return new NetworkBoundResource<List<StatusEntity>, List<StatusEntity>>() {
            @NonNull
            @Override
            protected Flowable<List<StatusEntity>> createApi() {
                return WeiboComponent.accountSdk.getAccount()
                        .flatMap(new Function<Account, Publisher<List<StatusEntity>>>() {

                            @Override
                            public Publisher<List<StatusEntity>> apply(Account account) throws Exception {
                                return mWeiboApis
                                        .getAllStatus(account.getToken())
                                        .map(new Function<StatusResult, List<StatusEntity>>() {
                                            @Override
                                            public List<StatusEntity> apply(StatusResult statusResult) throws Exception {
                                                if (statusResult != null && statusResult.statusList != null) {
                                                    return statusResult.statusList;
                                                }
                                                return null;
                                            }
                                        });
                            }
                        });
            }

            @Override
            protected void saveCallResult(@NonNull List<StatusEntity> item) {
                mAppDataBase.statusDao().insertStatusEntities(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StatusEntity> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<StatusEntity>> loadFromDb() {
                return mAppDataBase.statusDao().getStatus();
            }
        }.getAsLiveData();
    }

    @Override
    public LiveData<Resource<UserEntity>> getUserInfo(long uid) {

        return new NetworkBoundResource<UserEntity, UserEntity>() {
            @NonNull
            @Override
            protected Flowable<UserEntity> createApi() {
                return WeiboComponent.accountSdk.getAccount()
                        .flatMap(new Function<Account, Publisher<UserEntity>>() {
                            @Override
                            public Publisher<UserEntity> apply(Account account) throws Exception {
                                return mWeiboApis.getUser(account.getToken(), account.getUid());
                            }
                        });
            }

            @Override
            protected void saveCallResult(@NonNull UserEntity item) {
                if (item != null) {
                    mAppDataBase.userDao().insertUser(item);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return mAppDataBase.userDao().getUser();
            }
        }.getAsLiveData();
    }
}
