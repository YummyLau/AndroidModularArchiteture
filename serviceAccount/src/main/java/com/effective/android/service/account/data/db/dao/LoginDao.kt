package com.effective.android.service.account.data.db.dao

import androidx.room.*
import com.effective.android.service.account.data.db.entity.LoginInfoEntity

@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(info: LoginInfoEntity)

    @Delete
    fun delete(info: LoginInfoEntity)

    @Update
    fun update(info: LoginInfoEntity)

    @Query("select * from login_info")
    fun getAll():MutableList<LoginInfoEntity>
}