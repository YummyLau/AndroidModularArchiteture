package example.weibocomponent.data.local.db.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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
