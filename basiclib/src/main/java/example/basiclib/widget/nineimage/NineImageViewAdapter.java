package example.basiclib.widget.nineimage;

import android.content.Context;
import android.widget.ImageView;

import com.jaeger.ninegridimageview.GridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.util.List;

/**
 * 覆盖扩展{@link NineGridImageViewAdapter}
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public abstract class NineImageViewAdapter<T> {

    protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

    protected void onItemImageClick(Context context, List<ImageView> imageViews, int index, List<T> list) {
    }

    protected ImageView generateImageView(Context context) {
        GridImageView imageView = new GridImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
