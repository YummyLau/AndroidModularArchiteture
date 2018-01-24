package example.basiclib.net.resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import example.basiclib.net.ObjectUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 定义仓库资源处理逻辑
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/13.
 * <a href="https://developer.android.com/topic/libraries/architecture/guide.html#addendum">NetworkBoundResource</a>
 */

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.loading((ResultType) null));
        final LiveData<ResultType> dbSource = loadFromDb();       //拉取数据库信息
        result.addSource(dbSource, new Observer<ResultType>() {   //监听数据
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.removeSource(dbSource);                    //解绑数据监听
                if (shouldFetch(resultType)) {                    //是否需要网络拉取
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            setValue(Resource.success(resultType));
                        }
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!ObjectUtils.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        result.addSource(dbSource, new Observer<ResultType>() {  //监听数据
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.setValue(Resource.loading(resultType));
            }
        });
        createApi()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<RequestType>() {
                    @Override
                    public void accept(RequestType requestType) throws Exception {
                        saveCallResult(requestType);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RequestType>() {
                    @Override
                    public void accept(RequestType requestType) throws Exception {
                        result.removeSource(dbSource);
                        result.addSource(loadFromDb(), new Observer<ResultType>() {
                            @Override
                            public void onChanged(@Nullable ResultType resultType) {
                                result.setValue(Resource.success(resultType));
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        onFetchFailed();
                        result.removeSource(dbSource);
                        result.addSource(dbSource, new Observer<ResultType>() {
                            @Override
                            public void onChanged(@Nullable ResultType resultType) {
                                result.setValue(Resource.error(throwable.getMessage(), resultType));
                            }
                        });
                    }
                });
    }

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract Flowable<RequestType> createApi();


    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);


    /**
     * 根据数据库取出的数据来决定是否需要进行网络请求
     *
     * @param data
     * @return
     */
    @MainThread
    protected boolean shouldFetch(@Nullable ResultType data) {
        return true;
    }

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();


    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
