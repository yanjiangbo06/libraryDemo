package cn.com.venvy.common.image;


/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class ImageFactory {

    public enum ImageLoaderType {
        Fresco,
        Glide
    }

    public IImageLoader createImageLoader(ImageLoaderType type) {
        IImageLoader imageLoader = null;
        switch (type) {
            case Glide:
                imageLoader = new GlideImageLoader();
                break;

            case Fresco:
                imageLoader = new FrescoImageLoader();
                break;
        }
        return imageLoader;
    }
}
