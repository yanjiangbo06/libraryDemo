package cn.com.venvy.common.http;

import android.util.SparseArray;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import cn.com.venvy.common.http.base.BaseRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.base.RequestType;
import cn.com.venvy.okhttp3.Call;
import cn.com.venvy.okhttp3.Callback;
import cn.com.venvy.okhttp3.FormBody;
import cn.com.venvy.okhttp3.OkHttpClient;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

class OkHttpHelper extends BaseRequestConnect {

    private OkHttpClient okHttpClient;
    private SparseArray<Call> callSparseArray = new SparseArray<>();

    public OkHttpHelper() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void get(final Request request) {
        cn.com.venvy.okhttp3.Request okRequest = parseRequest(request);
        final IRequestHandler handler = (IRequestHandler) getAllCallback().get(request.mRequestId);
        if (handler != null) {
            handler.startRequest(request);
        }
        Call call = okHttpClient.newCall(okRequest);
        startConnect(request, call, handler);
    }

    @Override
    public void post(Request request) {
        cn.com.venvy.okhttp3.Request okRequest = parseRequest(request);
        final IRequestHandler handler = (IRequestHandler) getAllCallback().get(request.mRequestId);
        if (handler != null) {
            handler.startRequest(request);
        }
        Call call = okHttpClient.newCall(okRequest);
        startConnect(request, call, handler);
    }

    private void startConnect(final Request request, Call call, final IRequestHandler handler) {

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final cn.com.venvy.okhttp3.Response response) throws IOException {

                if (handler != null) {
                    handler.requestFinish(request, new OKHttpResponse(response));
                }
                removeCallback(request);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                IRequestHandler handler = (IRequestHandler) getAllCallback().get(request.mRequestId);
                if (handler != null) {
                    handler.requestError(request, e);
                }
            }
        });
        callSparseArray.put(request.mRequestId, call);
    }


    private class OKHttpResponse implements IResponse {

        private cn.com.venvy.okhttp3.Response mResponse;

        public OKHttpResponse(cn.com.venvy.okhttp3.Response response) {
            mResponse = response;
        }

        @Override
        public Object getData() {
            return mResponse.body();
        }

        @Override
        public boolean isSuccess() {
            return mResponse.isSuccessful();
        }
    }

    @Override
    public boolean abort(Request request) {
        Call call = callSparseArray.get(request.mRequestId);
        if (call != null) {
            call.cancel();
        }
        return super.abort(request);
    }

    @Override
    public boolean abortAll() {
        okHttpClient.dispatcher().cancelAll();
        return super.abortAll();
    }

    private cn.com.venvy.okhttp3.Request parseRequest(Request request) {
        final cn.com.venvy.okhttp3.Request.Builder builder = new cn.com.venvy.okhttp3.Request.Builder();
        if (request.mHeaders != null && request.mHeaders.size() > 0) {
            Iterator<Map.Entry<String, String>> it = request.mHeaders.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (entry.getValue() != null) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        builder.url(request.url);
        if (request.mRequestType == RequestType.GET) {
            builder.get();
        } else if (request.mRequestType == RequestType.POST) {
            FormBody.Builder fromBuilder = new FormBody.Builder();
            if (request.mParams != null && request.mParams.size() > 0) {
                Iterator<Map.Entry<String, String>> it = request.mParams.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (entry.getValue() != null) {
                        fromBuilder.add(entry.getKey(), entry.getValue());
                    }
                }
            }
            builder.post(fromBuilder.build());
        }
        return builder.build();
    }
}