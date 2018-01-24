package example.weibocomponent.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import example.weibocomponent.data.local.db.entity.TimeZoneEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/26.
 */
@Dao
public interface TimeZoneDao {

    @Query("SELECT * FROM " + TimeZoneEntity.TABLE_NAME)
    LiveData<List<TimeZoneEntity>> getTimeZones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTimeZoneEntities(List<TimeZoneEntity> timeZoneEntities);
}
