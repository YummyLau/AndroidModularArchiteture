package example.weibocomponent.data.local.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * 时区表
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Entity(tableName = TimeZoneEntity.TABLE_NAME,
        primaryKeys = TimeZoneEntity.COLUMN_ID_NAME)
public class TimeZoneEntity {

    public static final String TABLE_NAME = "timezone_table";
    public static final String COLUMN_ID_NAME = "time_zone_id";
    public static final String COLUMN_STR_NAME = "time_zone_str";
    public static final String COLUMN_MSG_NAME = "time_zone_msg";

    /**
     *  "1" : "(GMT-11:00) 阿皮亚"
     *  id ： 1
     *  weiboStr ：GMT-11:00
     *  msg：(GMT-11:00) 阿皮亚
     */

    @ColumnInfo(index = true, name = COLUMN_ID_NAME)
    public long id;

    @ColumnInfo(index = true, name = COLUMN_STR_NAME)
    public String weiboStr;

    @ColumnInfo(index = true, name = COLUMN_MSG_NAME)
    public String msg;
}
