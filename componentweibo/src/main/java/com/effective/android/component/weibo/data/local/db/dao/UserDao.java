package com.effective.android.component.weibo.data.local.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.effective.android.component.weibo.data.local.db.entity.UserEntity;
import io.reactivex.Flowable;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/26.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(UserEntity user);

    @Query("SELECT * FROM " + UserEntity.TABLE_NAME + " LIMIT 1")
    LiveData<UserEntity> getUser();

    @Query("SELECT * FROM " + UserEntity.TABLE_NAME)
    Flowable<List<UserEntity>> getUsers();

}
