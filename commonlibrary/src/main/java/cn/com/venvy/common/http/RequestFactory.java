package cn.com.venvy.common.http;

import cn.com.venvy.common.http.base.IRequestConnect;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public class RequestFactory {

    public static IRequestConnect connect;

    public enum HttpPlugin {
        OK_HTTP,
        VOLLEY
    }

    public static void initConnect(HttpPlugin plugin) {
        switch (plugin) {
            case OK_HTTP:
                OkHttpHelper okHttpHelper = new OkHttpHelper();
                okHttpHelper.init();
                connect = okHttpHelper;
                break;

            case VOLLEY:
                VolleyHelper volleyHelper = new VolleyHelper();
                volleyHelper.init();
                connect = volleyHelper;
                break;
        }
    }

    public static IRequestConnect getRequestConnect() {
        return connect;
    }

    public static void setRequestConnect(IRequestConnect requestConnect) {
        connect = requestConnect;
    }
}
