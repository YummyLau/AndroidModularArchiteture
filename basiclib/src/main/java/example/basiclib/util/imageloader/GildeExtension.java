package example.basiclib.util.imageloader;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * 文档：https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/25.
 *
 * 被 @GlideExtention 注解的类有两种扩展方式：
 * GlideOption - 为 RequestOptions 添加一个自定义的选项。
 * GlideType - 添加对新的资源类型的支持(GIF，SVG 等等)。
 */
@GlideExtension
public class GildeExtension {

    // Size of mini thumb in pixels.
    private static final int MINI_THUMB_SIZE = 100;
//    private static final RequestOptions DECODE_TYPE_GIF = decodeTypeOf(GifDrawable.class).lock();

    private GildeExtension() { } // utility class


    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options.fitCenter()
                .override(MINI_THUMB_SIZE);
    }

//    @GlideType(GifDrawable.class)
//    public static void asGif(RequestBuilder<GifDrawable> requestBuilder) {
//        requestBuilder
//                .transition(new DrawableTransitionOptions())
//                .apply(DECODE_TYPE_GIF);
//    }
}
