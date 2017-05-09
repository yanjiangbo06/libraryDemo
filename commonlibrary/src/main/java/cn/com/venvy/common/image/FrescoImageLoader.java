package cn.com.venvy.common.image;

import android.content.Context;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

class FrescoImageLoader implements IImageLoader {

    public FrescoImageLoader(Context context) {
        Fresco.initialize(context);
    }

    @Override
    public void loadImage(ImageView imageView, String url) {

    }
}
