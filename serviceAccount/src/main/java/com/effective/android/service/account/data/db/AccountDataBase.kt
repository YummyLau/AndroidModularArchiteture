package com.effective.android.service.account.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.effective.android.service.account.data.db.dao.LoginDao
import com.effective.android.service.account.data.db.entity.LoginInfoEntity

@Database(entities = [LoginInfoEntity::class], version = 1)
abstract class AccountDataBase : RoomDatabase() {

    abstract fun getLoginDao(): LoginDao

    companion object {
        //手动初始化
        fun initDataBase(application: Application) {
            instance = Room.databaseBuilder(
                    application,
                    AccountDataBase::class.java,
                    "account.db").allowMainThreadQueries().build()
        }

        lateinit var instance: AccountDataBase
    }

}