package com.effective.android.service.account

/**
 * 行为信息，归属账户模块管理
 * created by yummylau on 2020/05/27
 */
open class ActionInfo{

    open var shareCount: Long = 0L
    open var collectCount: Long = 0L

    companion object {
        fun createEmpty(): ActionInfo = ActionInfo()
    }
}