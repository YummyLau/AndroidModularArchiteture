package com.effective.android.lib_imageloader.modelloaders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.nio.ByteBuffer;

/**
 * 支持base64请求图片
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class Base64ModelLoader implements ModelLoader<String, ByteBuffer> {

    private static final String DATA_URI_PREFIX = "data:";

    @Nullable
    @Override
    public LoadData<ByteBuffer> buildLoadData(@NonNull String s, int width, int height, @NonNull Options options) {
        //将被用于磁盘缓存键的一部分 (模型的 equals 和 hashCode() 方法被用于内存缓存键
        //可以从我们的特定模型中建立一个 ByteBuffer，这里fetcher不能为null
        return new LoadData<>(new ObjectKey(s), new Base64DataFetcher(s));
    }

    @Override
    public boolean handles(@NonNull String s) {
        return s.startsWith(DATA_URI_PREFIX);
    }


    /**
     * 从我们的特定模型中建立一个 ByteBuffer
     */
    public class Base64DataFetcher implements DataFetcher<ByteBuffer> {

        private String model;

        public Base64DataFetcher(String model) {
            this.model = model;
        }

        @Override
        public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super ByteBuffer> callback) {
            //这个方法只会被某个后台线程所调用，一个给定的 DataFetcher 在同一时间只会被一个后台线程使用，因此它不需要做到线程安全。然而，多个 DataFetcher 可能会被并行执行，因此 DataFetcher 所访问的任何共享资源应当是线程安全的
            String base64Section = getBase64SectionOfModel();
            byte[] data = Base64.decode(base64Section, Base64.DEFAULT);
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            callback.onDataReady(byteBuffer);
        }

        private String getBase64SectionOfModel() {
            // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs.
            int startOfBase64Section = model.indexOf(',');
            return model.substring(startOfBase64Section + 1);
        }

        @Override
        public void cleanup() {
            // Intentionally empty only because we're not opening an InputStream or another I/O resource!
        }

        @Override
        public void cancel() {
            //可以取消的网络连接库或长时间加载
        }

        @NonNull
        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }
}
