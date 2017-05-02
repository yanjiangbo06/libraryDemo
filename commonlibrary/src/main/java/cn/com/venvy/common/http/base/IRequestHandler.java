package cn.com.venvy.common.http.base;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public interface IRequestHandler {

    void requestFinish(Request request, IResponse response);

    void requestError(Request request, Exception e);

    void startRequest(Request request);

    /**
     * 暂时还不支持，先预埋
     * @param request
     * @param progress
     */
    void requestProgress(Request request, int progress);
}
