package example.weibocomponent.data;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.basiclib.net.resource.NetworkBoundResource;
import example.basiclib.net.resource.Resource;
import example.componentlib.service.ServiceManager;
import example.componentlib.service.account.Account;
import example.componentlib.service.account.IAccountService;
import example.weibocomponent.data.local.db.AppDataBase;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.data.local.db.entity.UserEntity;
import example.weibocomponent.data.remote.api.WeiboApis;
import example.weibocomponent.data.remote.result.StatusResult;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
@Singleton
public class DemoRepository implements DemoDataSource {

    private AppDataBase mAppDataBase;
    private WeiboApis mWeiboApis;

    @Inject
    public DemoRepository(AppDataBase appDataBase, WeiboApis weiboApis) {
        this.mAppDataBase = appDataBase;
        this.mWeiboApis = weiboApis;
        ARouter.getInstance().inject(this);
    }


    @Override
    public LiveData<Resource<List<StatusEntity>>> getHomeStatus() {
        return new NetworkBoundResource<List<StatusEntity>, List<StatusEntity>>() {
            @NonNull
            @Override
            protected Flowable<List<StatusEntity>> createApi() {
                return ServiceManager.getService(IAccountService.class).getAccount()
                        .flatMap(new Function<Account, Publisher<List<StatusEntity>>>() {

                            @Override
                            public Publisher<List<StatusEntity>> apply(Account account) throws Exception {
                                return mWeiboApis
                                        .getAllStatus(account.token)
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
                return ServiceManager.getService(IAccountService.class).getAccount()
                        .flatMap(new Function<Account, Publisher<UserEntity>>() {
                            @Override
                            public Publisher<UserEntity> apply(Account account) throws Exception {
                                return mWeiboApis.getUser(account.token, account.uid);
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
