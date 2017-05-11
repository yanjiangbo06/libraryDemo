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
    abstract  class RequestHandlerAdapter implements  IRequestHandler{
        @Override
        public void requestFinish(Request request, IResponse response) {

        }
        @Override
        public void requestError(Request request, Exception e) {

        }
        @Override
        public void startRequest(Request request) {

        }
        @Override
        public void requestProgress(Request request, int progress) {

        }
    }
}
