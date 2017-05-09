package cn.com.venvy.common.image;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

class FrescoImageLoader implements IImageLoader {


    @Override
    public void loadImage(ImageView imageView, String url) {
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(imageView.getContext());
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (imageView instanceof SimpleDraweeView) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
            simpleDraweeView.setImageURI(Uri.parse(url));
        } else {
            VenvyLog.e("Fresco need SimpleDraweeView");
        }
    }

    public void loadImage(ImageView imageView, String fileName, boolean isFile) {
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(imageView.getContext());
        }
        if (!isFile) {
            loadImage(imageView, fileName);
        }
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        if (imageView instanceof SimpleDraweeView) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
            simpleDraweeView.setImageURI(Uri.parse(fileName));
        } else {
            VenvyLog.e("Fresco need SimpleDraweeView");
        }
    }
}
