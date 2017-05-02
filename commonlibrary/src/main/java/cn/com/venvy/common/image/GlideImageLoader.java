package cn.com.venvy.common.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

 class GlideImageLoader implements IImageLoader {

    public GlideImageLoader(Context context) {

    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        Context context = imageView.getContext();
    }
}
