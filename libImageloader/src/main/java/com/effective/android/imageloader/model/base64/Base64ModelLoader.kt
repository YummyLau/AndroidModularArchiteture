package com.effective.android.imageloader.model.base64

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey
import java.nio.ByteBuffer

/**
 * 支持base64请求图片
 * Created by yummyLau on 2019/03/22.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class Base64ModelLoader : ModelLoader<String, ByteBuffer> {

    private val dataPrefix = "data:"

    override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<ByteBuffer>? {
        //将被用于磁盘缓存键的一部分 (模型的 equals 和 hashCode() 方法被用于内存缓存键
        //可以从我们的特定模型中建立一个 ByteBuffer，这里fetcher不能为null
        return ModelLoader.LoadData(ObjectKey(model), Base64DataFetcher(model))
    }

    override fun handles(model: String): Boolean = model.startsWith(dataPrefix)
}