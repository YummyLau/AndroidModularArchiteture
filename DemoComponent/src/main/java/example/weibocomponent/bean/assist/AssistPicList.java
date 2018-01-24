package example.weibocomponent.bean.assist;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import example.weibocomponent.bean.Pic;

/**
 * 辅助处理List<T> room Converters
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class AssistPicList {

    public static final String ITEM_DIVIDER = "~";

    public List<Pic> picList;

    public AssistPicList() {
    }

    public AssistPicList(String picStr) {
        if (!TextUtils.isEmpty(picStr)) {
            String[] strs = picStr.split(ITEM_DIVIDER);
            if (strs != null && strs.length > 0) {
                picList = new ArrayList<>();
                for (String str : strs) {
                    picList.add(new Pic(str));
                }
            }
        }
    }

    @Nullable
    public String getPicStr() {
        if (picList == null || picList.isEmpty()) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Pic pic : picList) {
                stringBuilder.append(pic.thumbnailPic);
                stringBuilder.append(ITEM_DIVIDER);
            }
            String result = stringBuilder.toString();
            return result.endsWith(ITEM_DIVIDER) ?
                    result.substring(0, result.length() - 1) : result;
        }
    }
}
