package cn.com.venvy.common.image;


import android.widget.ImageView;

import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class ImageFactory {

    private static IImageLoader sImageLoader = null;

    public enum ImageLoaderType {
        Fresco,
        Glide
    }

    public static void initImageLoader(ImageLoaderType type) {

        switch (type) {
            case Glide:
                sImageLoader = new GlideImageLoader();
                break;

            case Fresco:
                sImageLoader = new FrescoImageLoader();
                break;
        }
    }

    public static IImageLoader getImageLoader() {
        return sImageLoader;
    }

    public static void setImageLoader(IImageLoader imageLoader) {
        sImageLoader = imageLoader;
    }

    public static void loadImage(ImageView imageView, String url) {
        if (sImageLoader == null) {
            VenvyLog.e("imageLoader not init");
            return;
        }
        sImageLoader.loadImage(imageView, url);
    }

    public static void loadImage(ImageView imageView, String fileName, boolean isFile) {
        if (sImageLoader == null) {
            VenvyLog.e("imageLoader not init");
            return;
        }
        sImageLoader.loadImage(imageView, fileName, isFile);
    }
}
