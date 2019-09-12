package com.effective.android.service.account.data

import android.app.Application
import android.text.TextUtils
import com.effective.android.base.util.GsonUtils
import com.effective.android.service.account.UserInfo
import java.io.File


/**
 * 账号模块仓库
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
class AccountRepository(private val application: Application) : AccountDataSource {

    val USER_File: String = "user_file"
    val USER_KEY: String = "user_key"
    var userInfo: UserInfo? = null

    init {
        var userString = application.getSharedPreferences(USER_File, android.content.Context.MODE_PRIVATE).getString(USER_KEY, "")
        if (!TextUtils.isEmpty(userString)) {
            userInfo = GsonUtils.getObj(userString, UserInfo::class.java)
        }
    }

    override fun isLogin(): Boolean = userInfo != null

    override fun login(username: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
