package cn.com.venvy.common.http.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import java.util.HashMap;

import cn.com.venvy.Platform;
import cn.com.venvy.PlatformInfo;
import cn.com.venvy.common.utils.VenvyDeviceUtil;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyPackageUtil;
import cn.com.venvy.common.utils.VenvyUIUtil;


/**
 * Created by yanjiangbo on 2017/4/27.
 */

public abstract class BaseRequestConnect implements IRequestConnect {

    protected static final String TAG = "BaseRequestConnect";

    private volatile SparseArray<IRequestHandler> requestCallBackArray = new SparseArray<>();

    private HashMap<String, String> mDefaultHeaders;

    public void init() {
        mDefaultHeaders = buildDefaultUrlHeaders();
    }

    @Override
    public void connect(Request request, IRequestHandler handler) {
        if (request == null) {
            VenvyLog.w("request can't be null, please check");
            return;
        }
        if (VenvyUIUtil.isOnUIThread()) {
            //如果是主线程的调用，每次都处理下头部信息
            mDefaultHeaders = buildDefaultUrlHeaders();
        }
        if (mDefaultHeaders != null) {
            HashMap<String, String> headers = request.mHeaders;
            if (headers != null) {
                headers.putAll(mDefaultHeaders);
            } else {
                headers = mDefaultHeaders;
            }
            request.mHeaders = headers;
        }
        addCallBack(request, handler);
        if (request.mRequestType == RequestType.GET) {
            get(request);
        } else if (request.mRequestType == RequestType.POST) {
            post(request);
        } else if (request.mRequestType == RequestType.PUT) {
            put(request);
        } else if (request.mRequestType == RequestType.DELETE) {
            delete(request);
        }
        VenvyLog.i(TAG,"start Request, Url = " + request.url);
    }

    public boolean abort(Request request) {
        if (request == null) {
            VenvyLog.w("request can't be null, please check");
            return false;
        }
        abortRequest(request);
        if (requestCallBackArray.get(request.mRequestId) != null) {
            requestCallBackArray.delete(request.mRequestId);
            return true;
        } else {
            return false;
        }
    }

    public boolean abortAll() {
        requestCallBackArray.clear();
        abortAllRequest();
        return true;
    }

    protected SparseArray getAllCallback() {
        return requestCallBackArray;
    }

    protected void removeCallback(Request request) {
        requestCallBackArray.remove(request.mRequestId);
    }

    protected void addCallBack(Request request, IRequestHandler callBack) {
        if (callBack != null) {
            requestCallBackArray.put(request.mRequestId, callBack);
        }
    }

    protected Object getCallBack(Request request) {
        return requestCallBackArray.get(request.mRequestId);
    }

    public abstract void get(Request request);

    public abstract void post(Request request);

    public abstract void put(Request request);

    public abstract void delete(Request request);

    public abstract void abortRequest(Request request);

    public abstract void abortAllRequest();

    private static final String VERSION = "version";
    private static final String SDK_VERSION = "sdk-version";
    private static final String USER_AGENT = "User-Agent";
    private static final String OS_VERSION = "os-version";
    private static final String UDID = "udid";
    private static final String TOKEN = "token";
    private static final String IP = "ip";
    private static final String NETWORK = "network";
    private static final String PLATFORM_ID = "3rd-platform-id";
    private static final String CYTRON_VERSION = "cytory-version";
    private static final String LAUGUAGE = "language";
    private static final String BU = "bu-id";

    private HashMap<String, String> buildDefaultUrlHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        PlatformInfo platformInfo = Platform.instance().getPlatformInfo();
        Context context = Platform.instance().getContext();
        if (context != null) {
            try {
                headers.put(VERSION, VenvyPackageUtil.getPackageVersion(context));
                headers.put(UDID, VenvyDeviceUtil.getDeviceUuid(context).toString());
                headers.put(NETWORK, VenvyDeviceUtil.getNetWorkName(context));
                headers.put(LAUGUAGE, VenvyDeviceUtil.getLanguage(context));
            } catch (PackageManager.NameNotFoundException e) {
                VenvyLog.e(TAG, e);
            }
        }
        if (platformInfo != null) {
            headers.put(SDK_VERSION, platformInfo.getSdkVersion());
            headers.put(PLATFORM_ID, platformInfo.getThirdPlatformId());
            headers.put(CYTRON_VERSION, platformInfo.getServiceVersion());
            headers.put(BU, platformInfo.getBuId());
        }
        headers.put(TOKEN, "");
        headers.put(USER_AGENT, VenvyDeviceUtil.getUserAgent());
        headers.put(OS_VERSION, VenvyDeviceUtil.getOsVersion());
        headers.put(IP, VenvyDeviceUtil.getLocalIPAddress());
        return headers;
    }
}
