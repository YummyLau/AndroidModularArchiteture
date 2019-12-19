package com.effective.android.component.tab.recommendation.data

import androidx.arch.core.util.Function
import com.effective.android.base.rxjava.RxCreator
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.Sdks
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.Type
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import java.util.concurrent.Callable

class RecommendRepository private constructor() {

    private val resommendApis by lazy {
        Sdks.serviceNet.service(
                RecommendApis.BASE_URL, Type.GSON, RecommendApis::class.java)
    }


    companion object {
        private var instance: RecommendRepository? = null
            get() {
                if (field == null) {
                    field = RecommendRepository()
                }
                return field
            }

        @Synchronized
        fun get(): RecommendRepository {
            return instance!!
        }
    }

    /**
     * 首页列表数据请求
     * 当pageIndex = 0时，请求banner+top+articles；
     * 当pageIndex>0 时，请求articles；
     */
    fun getRecommendList(pageIndex: Int): Flowable<BaseListResult<Article>> {
        return Flowable.zip(resommendApis.getNewArticles(pageIndex.toString()),
                resommendApis.getNewProjects(pageIndex.toString()),
                BiFunction<BaseListResult<Article>, BaseListResult<Article>, BaseListResult<Article>> { t1, t2 ->
                    if (t1.isSuccess && !(t1.data?.data.isNullOrEmpty())) {
                        if (t2.isSuccess && !(t2.data?.data.isNullOrEmpty())) {
                            t1.data?.data?.addAll(t2.data?.data!!)
                        }
                        t1
                    }
                    if (t2.isSuccess && !(t2.data?.data.isNullOrEmpty())) {
                        if (t1.isSuccess && !(t1.data?.data.isNullOrEmpty())) {
                            t2.data?.data?.addAll(t1.data?.data!!)
                        }
                        t2
                    }
                    t1
                })
    }

}