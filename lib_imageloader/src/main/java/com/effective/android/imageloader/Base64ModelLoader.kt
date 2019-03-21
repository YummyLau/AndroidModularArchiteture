package com.effective.android.imageloader

import android.util.Base64

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey

import java.nio.ByteBuffer

/**
 * 支持base64请求图片
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class Base64ModelLoader : ModelLoader<String, ByteBuffer> {

    override fun buildLoadData(s: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<ByteBuffer>? {
        //将被用于磁盘缓存键的一部分 (模型的 equals 和 hashCode() 方法被用于内存缓存键
        //可以从我们的特定模型中建立一个 ByteBuffer，这里fetcher不能为null
        return ModelLoader.LoadData(ObjectKey(s), Base64DataFetcher(s))
    }

    override fun handles(s: String): Boolean {
        return s.startsWith(DATA_URI_PREFIX)
    }


    /**
     * 从我们的特定模型中建立一个 ByteBuffer
     */
    inner class Base64DataFetcher(private val model: String) : DataFetcher<ByteBuffer> {

        private// See https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs.
        val base64SectionOfModel: String
            get() {
                val startOfBase64Section = model.indexOf(',')
                return model.substring(startOfBase64Section + 1)
            }

        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in ByteBuffer>) {
            //这个方法只会被某个后台线程所调用，一个给定的 DataFetcher 在同一时间只会被一个后台线程使用，因此它不需要做到线程安全。然而，多个 DataFetcher 可能会被并行执行，因此 DataFetcher 所访问的任何共享资源应当是线程安全的
            val base64Section = base64SectionOfModel
            val data = Base64.decode(base64Section, Base64.DEFAULT)
            val byteBuffer = ByteBuffer.wrap(data)
            callback.onDataReady(byteBuffer)
        }

        override fun cleanup() {
            // Intentionally empty only because we're not opening an InputStream or another I/O resource!
        }

        override fun cancel() {
            //可以取消的网络连接库或长时间加载
        }

        override fun getDataClass(): Class<ByteBuffer> {
            return ByteBuffer::class.java
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }
    }

    companion object {

        private val DATA_URI_PREFIX = "data:"
    }
}
