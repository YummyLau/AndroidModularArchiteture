package example.weibocomponent.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * weibo pic
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class Pic {

    public static final String THUMBNAIL = "thumbnail";
    public static final String BMIDDLE = "bmiddle";
    public static final String LARGE = "large";

    @SerializedName("thumbnail_pic")
    public String thumbnailPic;

    public Pic(String thumbnailPic) {
        this.thumbnailPic = thumbnailPic;
    }

    public String getSmallPic() {
        return thumbnailPic;
    }

    public String getMidPic() {
        if (!TextUtils.isEmpty(thumbnailPic)) {
            return thumbnailPic.replace(THUMBNAIL, BMIDDLE);
        }
        return thumbnailPic;
    }

    public String getLargePic() {
        if (!TextUtils.isEmpty(thumbnailPic)) {
            return thumbnailPic.replace(THUMBNAIL, LARGE);
        }
        return thumbnailPic;
    }
}
