package cn.com.venvy.common.http.base;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public interface IRequestConnect {

    void connect(Request request, IRequestHandler handler);

    RequestConnectStatus getConnectStatus();

    boolean abort(Request request);

    boolean abortAll();
}
