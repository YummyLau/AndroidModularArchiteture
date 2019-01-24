package example.weibocomponent.data.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import example.weibocomponent.data.local.db.dao.StatusDao;
import example.weibocomponent.data.local.db.dao.UserDao;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.data.local.db.entity.TimeZoneEntity;
import example.weibocomponent.data.local.db.entity.UserEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Database(entities = {UserEntity.class, StatusEntity.class, TimeZoneEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract StatusDao statusDao();

    private static final Object sLock = new Object();
    private static AppDataBase INSTANCE;
    public static final String DB_FILE_NAME = "feature_component.db";

    public static AppDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, DB_FILE_NAME)
                        .build();
            }
            return INSTANCE;
        }
    }
}
