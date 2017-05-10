package cn.com.venvy.common.http.base;

/**
 * Created by yanjiangbo on 2017/5/10.
 */

public class RequestIDHelper {

    private static RequestIDHelper sRequestIDHelper;

    private int requestID = Integer.MIN_VALUE;

    public synchronized static RequestIDHelper getInstance() {
        if (sRequestIDHelper == null) {
            sRequestIDHelper = new RequestIDHelper();
        }
        return sRequestIDHelper;
    }

    public int getRequestId() {
        requestID = requestID++;
        if (requestID >= Integer.MAX_VALUE) {
            requestID = Integer.MIN_VALUE;
            getRequestId();
        }
        return requestID;
    }
}
