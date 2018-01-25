package example.basiclib.interfaces;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.request.FutureTarget;

/**
 * 图片处理暴露接口，方便以后替换第三方库处理
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public interface IImageLoader {

    void load(Context context, String url, ImageView imageView);

    void load(Fragment fragment, String url, ImageView imageView);

    void load(Activity activity, String url, ImageView imageView);

    void load(Activity activity, String url, ImageView imageView,
              int width, int height,
              @DrawableRes int placeholderImg,
              @DrawableRes int fallbackImg,
              @DrawableRes int errorImg);
}
