package com.effective.android.lib_imageloader.modelloaders;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.nio.ByteBuffer;

/**
 * base64 loaderFactory
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class Base64ModelLoaderFactory implements ModelLoaderFactory<String,ByteBuffer>{

    @NonNull
    @Override
    public ModelLoader<String, ByteBuffer> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new Base64ModelLoader();
    }

    @Override
    public void teardown() {

    }
}
