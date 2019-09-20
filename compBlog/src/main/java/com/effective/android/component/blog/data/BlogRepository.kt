package com.effective.android.component.blog.data

import com.effective.android.base.rxjava.RxCreator
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.Sdks
import com.effective.android.component.blog.bean.*
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import com.effective.android.service.net.Type
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import java.util.concurrent.Callable

/**
 * 单例仓库类管理
 * created by yummylau on 2019/09/18
 */
class BlogRepository private constructor() {

    private val blogApis by lazy {
        Sdks.serviceNet.service(
                BlogApis.BASE_URL, Type.GSON, BlogApis::class.java)
    }
    

    companion object {
        private var instance: BlogRepository? = null
            get() {
                if (field == null) {
                    field = BlogRepository()
                }
                return field
            }

        @Synchronized
        fun get(): BlogRepository {
            return instance!!
        }
    }

    /**
     * 首页列表数据请求
     * 当pageIndex = 0时，请求banner+top+articles；
     * 当pageIndex>0 时，请求articles；
     */
    fun getBlogList(pageIndex: Int): Flowable<BaseListResult<IMediaItem>> {
        return if (pageIndex > 0) {
            blogApis.getArticles(pageIndex.toString())
                    .flatMap {
                        RxCreator.createFlowable(Callable<BaseListResult<IMediaItem>> {
                            it.transform()
                        })
                    }
        } else {
            Flowable.zip(
                    blogApis.getArticles(pageIndex.toString()),
                    blogApis.getBanner(),
                    blogApis.getTopArticles(),
                    Function3<BaseListResult<Article>, BaseResult<List<Banner>>, BaseResult<List<Article>>, BaseListResult<IMediaItem>> { t1, t2, t3 ->
                        var result = t1.transform<IMediaItem>()
                        if (t3.isSuccess && !t3.data.isNullOrEmpty()) {
                            result.data?.data?.addAll(0, t3.data!!)
                        }
                        if (t2.isSuccess && !t2.data.isNullOrEmpty()) {
                            val bannerList = BannerList()
                            for(banner in t2.data!!){
                                if(banner.isVisible == 1){
                                    bannerList.add(0,banner)
                                }
                            }
                            if(!bannerList.isNullOrEmpty()){
                                result.data?.data?.add(0, bannerList)
                            }
                        }
                        result
                    }
            )
        }
    }


    fun getFriendWebsize(): Flowable<BaseResult<List<Website>>> = blogApis.getFriendWebsize()

    fun getHotSearchKey(): Flowable<BaseResult<List<SearchKey>>> = blogApis.getHotSearchKey()
}