package example.basiclib.event;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class RefreshSkinEvent {

    public Integer color;

    public RefreshSkinEvent(@ColorInt @NonNull Integer color) {
        this.color = color;
    }
}
