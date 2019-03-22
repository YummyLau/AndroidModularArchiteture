package com.effective.android.imageloader.model.base64

import android.util.Base64
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.nio.ByteBuffer

/**
 *
 * base64请求拉取数据
 * Glide 已经支持了 Data URI，所以如果你只是想使用 Data URI 加载 Base64 字符串的话并不需要做什么
 * 如何编写一个自动以model可参考 https://muyangmin.github.io/glide-docs-cn/tut/custom-modelloader.html
 *
 * 格式：https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
 * String dataUri = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYA..."
 * Glide.with(fragment)
 * .load(dataUri)
 * .into(imageView);
 *
 * Created by yummyLau on 2019/03/22.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class Base64DataFetcher(private val model: String) : DataFetcher<ByteBuffer> {

    private val base64SectionOfModel: String
        get() {
            val startOfBase64Section = model.indexOf(',')
            return model.substring(startOfBase64Section + 1)
        }

    override fun getDataClass(): Class<ByteBuffer> {
        return ByteBuffer::class.java
    }

    override fun cleanup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in ByteBuffer>) {
        //这个方法只会被某个后台线程所调用，一个给定的 DataFetcher 在同一时间只会被一个后台线程使用，因此它不需要做到线程安全。然而，多个 DataFetcher 可能会被并行执行，因此 DataFetcher 所访问的任何共享资源应当是线程安全的
        val base64Section = base64SectionOfModel
        val data = Base64.decode(base64Section, Base64.DEFAULT)
        val byteBuffer = ByteBuffer.wrap(data)
        callback.onDataReady(byteBuffer)
    }
}

