package cn.com.venvy.common.http;

import android.content.Context;

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
                connect = new OkHttpHelper();
                break;

            case VOLLEY:
                connect = new VolleyHelper();
                break;
        }
        return connect;
    }
}
