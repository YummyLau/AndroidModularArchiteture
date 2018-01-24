package example.weibocomponent.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import example.weibocomponent.data.local.db.entity.UserEntity;
import io.reactivex.Flowable;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/4.
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
