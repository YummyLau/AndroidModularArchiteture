package example.componentweibo.data.local.db.dao;


import androidx.lifecycle.LiveData;

import java.util.List;

import example.componentweibo.data.local.db.entity.StatusEntity;

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
