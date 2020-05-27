package com.effective.android.service.account.data.bean

import com.effective.android.service.account.RankInfo
import com.effective.android.service.net.ListData
import com.google.gson.annotations.SerializedName

class TmpShareBean{

    @SerializedName("coinCount")
    var rankInfo:RankInfo? = null

    @SerializedName("shareArticles")
    var shareArticles:ListData<Any>? = null
}