package cn.com.venvy.common.http;

import cn.com.venvy.common.http.base.BaseRequestConnect;
import cn.com.venvy.common.http.base.Request;

/**
 * Created by yanjiangbo on 2017/4/27.
 */

class VolleyHelper extends BaseRequestConnect {


    @Override
    public void get(Request request) {

    }

    @Override
    public void post(Request request) {

    }

    @Override
    public boolean abort(Request request) {
        return super.abort(request);
    }

    @Override
    public boolean abortAll() {
        return super.abortAll();
    }
}