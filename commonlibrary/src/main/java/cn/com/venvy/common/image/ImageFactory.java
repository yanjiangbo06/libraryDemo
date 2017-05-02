package cn.com.venvy.common.image;

import android.content.Context;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class ImageFactory {

    public enum ImageLoaderType {
        Fresco,
        Glide
    }

    public IImageLoader createImageLoader(Context context, ImageLoaderType type) {
        IImageLoader imageLoader = null;
        switch (type) {
            case Glide:
                imageLoader = new GlideImageLoader(context);
                break;

            case Fresco:
                imageLoader = new FrescoImageLoader(context);
                break;
        }
        return imageLoader;
    }
}
