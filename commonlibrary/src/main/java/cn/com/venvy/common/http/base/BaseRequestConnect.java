package cn.com.venvy.common.http.base;

import android.support.annotation.Nullable;
import android.util.SparseArray;


/**
 * Created by yanjiangbo on 2017/4/27.
 */

public abstract class BaseRequestConnect implements IRequestConnect {

    private static final String TAG = "BaseRequestConnect";

    private SparseArray<IRequestHandler> requestCallBackArray = new SparseArray<>();


    @Override
    public void connect(@Nullable Request request, IRequestHandler handler) {
        addCallBack(request, handler);
        if (request.mRequestType == RequestType.GET) {
            get(request);
        } else if (request.mRequestType == RequestType.POST) {
            post(request);
        }
    }

    public boolean abort(Request request) {
        if (requestCallBackArray.get(request.mRequestId) != null) {
            requestCallBackArray.delete(request.mRequestId);
            return true;
        } else {
            return false;
        }

    }

    public boolean abortAll() {
        requestCallBackArray.clear();
        return true;
    }

    protected SparseArray getAllCallback() {
        return requestCallBackArray;
    }

    protected void removeCallback(Request request) {
        requestCallBackArray.remove(request.mRequestId);
    }

    protected void addCallBack(Request request, IRequestHandler callBack) {
        requestCallBackArray.put(request.mRequestId, callBack);
    }

    protected Object getCallBack(Request request) {
        return requestCallBackArray.get(request.mRequestId);
    }

    public abstract void get(Request request);

    public abstract void post(Request request);
}
