package example.weibocomponent.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import example.weibocomponent.data.local.db.entity.StatusEntity;

/**
 Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

@Dao
public interface StatusDao {

    @Query("SELECT * FROM status_table")
    LiveData<List<StatusEntity>> getStatus();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertStatusEntities(List<StatusEntity> statusEntities);
}
