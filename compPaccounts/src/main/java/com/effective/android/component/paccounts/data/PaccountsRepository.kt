package com.effective.android.component.paccounts.data

import com.effective.android.component.paccounts.Sdks
import com.effective.android.service.kit.data.Article
import com.effective.android.service.kit.data.Chapter
import com.effective.android.service.net.BaseListResult
import com.effective.android.service.net.BaseResult
import com.effective.android.service.net.Type
import io.reactivex.Flowable

class PaccountsRepository {


    private val paccountsApis by lazy {
        Sdks.serviceNet.service(
                PaccountApis.BASE_URL, Type.GSON, PaccountApis::class.java)
    }


    companion object {
        private var instance: PaccountsRepository? = null
            get() {
                if (field == null) {
                    field = PaccountsRepository()
                }
                return field
            }

        @Synchronized
        fun get(): PaccountsRepository {
            return instance!!
        }
    }


    fun getChapters(): Flowable<BaseResult<List<Chapter>>> = paccountsApis.getChapters()

    fun getArticles(id: String, pageCount: String): Flowable<BaseListResult<Article>> = paccountsApis.getArticles(id, pageCount)

    fun getArticlesBySearch(id: String, pageCount: String, key: String): Flowable<BaseListResult<Article>> = paccountsApis.getArticlesBySearch(id, pageCount, key)

}