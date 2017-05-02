package cn.com.venvy.common.http.base;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

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
    private Random random = new Random();

    public Request(String url, RequestType method, InputStream input,
                   RequestCacheType defaultCacheType,
                   HashMap<String, String> headers, HashMap<String, String> params, long timeout) {
        this.url = url;
        this.mTimeOut = timeout <= 0 ? TIME_OUT : timeout;
        this.mParams = params;
        this.mCacheType = defaultCacheType;
        this.mRequestType = method;
        this.mHeaders = headers;
        this.mRequestId = random.nextInt(100) * random.nextInt(100);
    }
}
