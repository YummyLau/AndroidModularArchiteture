package com.effective.android.service.account

/**
 * 积分信息，归属账户模块管理
 * created by yummylau on 2020/05/27
 */
open class RankInfo{
    open var coinCount: Long = 0L
    open var level: Long = 0L
    open var rank: Long = 0L
    open var userId: Long = 0L
    open var userName:String = ""

    companion object {
        fun createEmpty(): RankInfo = RankInfo()
    }
}