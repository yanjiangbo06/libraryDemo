package cn.com.venvy.common.http;

import cn.com.venvy.Platform;
import cn.com.venvy.common.http.base.IRequestConnect;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public class RequestFactory {

    public enum HttpPlugin {
        OK_HTTP,
        VOLLEY
    }

    public static IRequestConnect initConnect(HttpPlugin plugin, Platform platform) {
        IRequestConnect connect = null;
        switch (plugin) {
            case OK_HTTP:
                OkHttpHelper okHttpHelper = new OkHttpHelper();
                okHttpHelper.init(platform);
                connect = okHttpHelper;
                break;

            case VOLLEY:
                VolleyHelper volleyHelper = new VolleyHelper();
                volleyHelper.init(platform);
                connect = volleyHelper;
                break;
        }
        return connect;
    }
}
