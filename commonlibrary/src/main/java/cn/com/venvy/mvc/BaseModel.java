package cn.com.venvy.mvc;

import android.widget.ImageView;

import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.image.IImageLoader;
import cn.com.venvy.common.image.ImageFactory;
import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/4/28.
 */

public abstract class BaseModel {
    private IRequestConnect mConnect;
    private IImageLoader mImageLoader;

    public BaseModel() {
        mConnect = createRequestConnect();
        mImageLoader = createImageLoader();
    }

    public void startRequest(Request request, IRequestHandler handler) {
        if (getRequestConnect() != null) {
            getRequestConnect().connect(request, handler);
        } else {
            VenvyLog.e("Request", new NullPointerException("the request connect is null"));
        }
    }

    public void displayImage(ImageView imageView, String url) {
        if (getImageLoader() != null) {
            getImageLoader().loadImage(imageView, url);
        } else {
            VenvyLog.e("ImageLoader", new NullPointerException("the imageloader is null"));
        }
    }

    public IImageLoader getImageLoader() {
        return mImageLoader;
    }

    public IRequestConnect getRequestConnect() {
        return mConnect;
    }

    protected IRequestConnect createRequestConnect() {
        RequestFactory factory = new RequestFactory();
        return factory.createConnect(RequestFactory.HttpPlugin.VOLLEY);
    }

    protected IImageLoader createImageLoader() {
        ImageFactory factory = new ImageFactory();
        return factory.createImageLoader(ImageFactory.ImageLoaderType.Fresco);
    }
}
