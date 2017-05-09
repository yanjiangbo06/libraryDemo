package cn.com.venvy.common.http;

import cn.com.venvy.common.http.base.IRequestConnect;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public class RequestFactory {


    public enum HttpPlugin {
        OK_HTTP,
        VOLLEY
    }

    public IRequestConnect createConnect(HttpPlugin plugin) {
        IRequestConnect connect = null;
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
        return connect;
    }
}
