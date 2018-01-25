package example.basiclib.activity;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * solve apply skin to make statuabar colorful
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ActivityEvent {

    //refresh statuabar
    public static final int TYPE_STATUS_BAR_REFRESH = 0x01;
    public static final String TYPE_STATUS_BAR_COLOR = "statusBarColor";

    public int type;
    public Map<String, Object> extras;

    public ActivityEvent(int type, Map<String, Object> extras) {
        this.type = type;
        this.extras = extras;
    }

    @IntDef({TYPE_STATUS_BAR_REFRESH})
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActivityEventType {

    }
}
