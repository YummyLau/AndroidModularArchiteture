package example.weibocomponent.data.local.db.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import example.weibocomponent.bean.assist.AssistPicList;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/25.
 */

public class Converters {

    @Nullable
    @TypeConverter
    public static String fromAssitPicList(AssistPicList assistPicList) {
        if (assistPicList == null) {
            return null;
        } else {
            return assistPicList.getPicStr();
        }
    }

    @Nullable
    @TypeConverter
    public static AssistPicList fromPicStr(String picStr) {
        if (TextUtils.isEmpty(picStr)) {
            return null;
        } else {
            return new AssistPicList(picStr);
        }
    }
}
