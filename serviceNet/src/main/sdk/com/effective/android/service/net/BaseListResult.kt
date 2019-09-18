package com.effective.android.service.net


/**
 * 列表类型返回
 * Created by yummyLau on 2019/09/18
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class BaseListResult<T> : BaseResult<ListData<T>>() {

    /**
     * 注意使用时必须明白继承院系
     */
    fun <R> transform(): BaseListResult<R> {
        val result = BaseListResult<R>()
        try {
            result.data = ListData()
            result.data!!.curPage = data?.curPage!!
            result.data!!.offset = data?.offset!!
            result.data!!.over = data?.over!!
            result.data!!.pageCount = data?.pageCount!!
            result.data!!.size = data?.size!!
            result.data!!.total = data?.total!!
            val dataList = ArrayList<R>()
            if (data?.data != null && data?.data?.isNotEmpty()!!) {
                for (item in data?.data!!) {
                    dataList.add(item as R)
                }
            }
            result.data!!.data = dataList
            result.errorCode = errorCode
            result.errorMsg = errorMsg
        } catch (e: Exception) {
            //
        }
        return result
    }
}
