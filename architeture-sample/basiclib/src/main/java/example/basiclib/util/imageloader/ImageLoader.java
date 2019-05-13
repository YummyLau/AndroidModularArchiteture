package example.basiclib.util.imageloader;

import android.app.Activity;
import android.content.Context;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import androidx.fragment.app.Fragment;
import example.basiclib.interfaces.IImageLoader;


/**
 * 使用Glide v4实现{@link IImageLoader}
 * <p>
 * 文档：https://muyangmin.github.io/glide-docs-cn/
 * 构建 {@link com.bumptech.glide.RequestBuilder}
 * 占位符 {@link RequestOptions}
 * 过度 {@link Transformation}
 * <p>
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class ImageLoader implements IImageLoader {

    private static ImageLoader INSTANCE = new ImageLoader();

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public void load(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }


    @Override
    public void load(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .into(imageView);
    }

    @Override
    public void load(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .into(imageView);
    }

    @Override
    public void load(Activity activity, String url, ImageView imageView, int width, int height, int placeholderImg, int fallbackImg, int errorImg) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderImg)
                .fallback(fallbackImg)
                .error(errorImg);
        Glide.with(activity)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }
}
