package cn.com.venvy.common.http;

import android.util.SparseArray;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import cn.com.venvy.common.http.base.BaseRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.base.RequestConnectStatus;
import cn.com.venvy.common.utils.VenvyLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * Created by yanjiangbo on 2017/4/26.
 */

class OkHttpHelper extends BaseRequestConnect {

    private OkHttpClient okHttpClient;
    private SparseArray<Call> callSparseArray = new SparseArray<>();

    OkHttpHelper() {
        okHttpClient = new OkHttpClient();
        status = RequestConnectStatus.IDLE;
    }

    private RequestConnectStatus status = RequestConnectStatus.NULL;

    @Override
    public void get(final Request request) {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
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
        builder.get();
        startConnect(builder.build(), request);
    }

    @Override
    public void post(Request request) {

        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
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
        startConnect(builder.build(), request);
    }

    @Override
    public void put(Request request) {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
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
        builder.put(fromBuilder.build());
        startConnect(builder.build(), request);
    }

    @Override
    public void delete(Request request) {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
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
        builder.delete(fromBuilder.build());
        startConnect(builder.build(), request);
    }

    private void startConnect(final okhttp3.Request okRequest, final Request request) {

        final IRequestHandler handler = (IRequestHandler) getAllCallback().get(request.mRequestId);
        if (handler != null) {
            handler.startRequest(request);
        }
        status = RequestConnectStatus.ACTIVE;
        Call call = okHttpClient.newCall(okRequest);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (handler != null) {
                    handler.requestFinish(request, new OKHttpResponse(response));
                }
                removeCallback(request);
                status = RequestConnectStatus.IDLE;
                VenvyLog.i(TAG, "request end, url = " + request.url);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (handler != null) {
                    handler.requestError(request, e);
                }
                removeCallback(request);
                status = RequestConnectStatus.IDLE;
                VenvyLog.i(TAG, "request error, url = " + request.url);
            }
        });
        callSparseArray.put(request.mRequestId, call);
    }


    private class OKHttpResponse implements IResponse {

        private Response mResponse;
        public OKHttpResponse(Response response) {
            mResponse = response;
        }

        @Override
        public Object getHeaders() {
            return mResponse.headers();
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
    public void abortRequest(Request request) {
        Call call = callSparseArray.get(request.mRequestId);
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public void abortAllRequest() {
        okHttpClient.dispatcher().cancelAll();
    }

    @Override
    public RequestConnectStatus getConnectStatus() {
        return status;
    }
}
