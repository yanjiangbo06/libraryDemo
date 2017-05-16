package cn.com.venvy.common.http.base;

import java.io.InputStream;
import java.util.HashMap;

import cn.com.venvy.common.utils.VenvyIDHelper;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public abstract class Request {

    private static final int TIME_OUT = 10 * 1000; // 10秒超时

    public int mRequestId;
    public String url;
    public long mTimeOut;
    public HashMap<String, String> mParams;
    public HashMap<String, String> mHeaders;
    public RequestType mRequestType;
    public RequestCacheType mCacheType;

    public Request(String url, RequestType method, InputStream input,
                   RequestCacheType defaultCacheType,
                   HashMap<String, String> headers, HashMap<String, String> params, long timeout) {
        this.url = url;
        this.mTimeOut = timeout <= 0 ? TIME_OUT : timeout;
        this.mParams = params;
        this.mCacheType = defaultCacheType;
        this.mRequestType = method;
        this.mHeaders = headers;
        this.mRequestId = VenvyIDHelper.getInstance().getId();
    }

    public void setHeaders(HashMap<String,String> headers) {
        mHeaders = headers;
    }

    public void setParams(HashMap<String,String> params) {
        mParams = params;
    }

    public void setRequestCacheType(RequestCacheType type) {
        mCacheType = type;
    }

    public void setTimeOut(long timeOut) {
        mTimeOut = timeOut;
    }
}
