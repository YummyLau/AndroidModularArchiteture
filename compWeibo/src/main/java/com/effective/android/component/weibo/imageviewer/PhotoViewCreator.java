package com.effective.android.component.weibo.imageviewer;

import android.content.Context;

import com.github.chrisbanes.photoview.PhotoView;

/**
 * 构建pagerview查看项
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class PhotoViewCreator {

    public static PhotoView createPhotoView(Context context, String url) {
        PhotoView photoView = new PhotoView(context);
        // TODO: 2019-09-06  
//        ImageLoader.getInstance().load(context, url, photoView);
        return photoView;
    }
}
