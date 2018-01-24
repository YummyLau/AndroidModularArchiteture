package example.componentlib.service.skin;

import android.support.annotation.ColorInt;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/24.
 */

public class Skin {

    public String key;
    public String sourceName;
    public Integer color;

    public Skin(String key, String sourceName, @ColorInt Integer color) {
        this.key = key;
        this.sourceName = sourceName;
        this.color = color;
    }
}
