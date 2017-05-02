package cn.com.venvy.common.http;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.com.venvy.Platform;
import cn.com.venvy.PlatformInfo;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.base.RequestCacheType;
import cn.com.venvy.common.http.base.RequestType;
import cn.com.venvy.common.utils.VenvyDeviceUtil;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyPackageUtil;

/**
 * Created by yanjiangbo on 2017/4/26.
 */

public class HttpRequest extends Request {

    private static final String TAG = "HttpRequest";

    private static final String VERSION = "version";
    private static final String SDK_VERSION = "sdk-version";
    private static final String USER_AGENT = "user-agent";
    private static final String OS_VERSION = "os-version";
    private static final String UDID = "udid";
    private static final String TOKEN = "token";
    private static final String IP = "ip";
    private static final String NETWORK = "network";
    private static final String PLATFORM_ID = "platform-id";
    private static final String CYTRON_VERSION = "cytory-version";

    private HttpRequest(String url, RequestType method, InputStream input,
                        RequestCacheType defaultCacheType,
                        HashMap<String, String> headers, HashMap<String, String> params, long timeout) {
        super(url, method, input, defaultCacheType, headers, params, timeout);
    }

    public static HttpRequest get(Context context, @Nullable String url, HashMap<String, String> headers, HashMap<String, String> params, RequestCacheType defaultCacheType) {

        url = buildUrlWithParams(url, params);
        headers = buildUrlWithHeaders(context, headers);
        return new HttpRequest(url, RequestType.GET, null, defaultCacheType, headers, null, -1);
    }

    public static HttpRequest get(Context context, @Nullable String url, HashMap<String, String> params, RequestCacheType defaultCacheType) {
        return get(context, url, null, params, defaultCacheType);
    }

    public static HttpRequest get(Context context, @Nullable String url, HashMap<String, String> params) {
        return get(context, url, null, params, RequestCacheType.DEFAULT);
    }

    public static HttpRequest get(Context context, @Nullable String url, RequestCacheType defaultCacheType) {
        return get(context, url, null, null, defaultCacheType);
    }

    public static HttpRequest get(Context context, @Nullable String url) {
        return get(context, url, RequestCacheType.DEFAULT);
    }

    public static HttpRequest post(Context context, @Nullable String url, HashMap<String, String> headers, HashMap<String, String> params, RequestCacheType defaultCacheType) {
        headers = buildUrlWithHeaders(context, headers);
        return new HttpRequest(url, RequestType.POST, null, defaultCacheType, headers, params, -1);
    }

    public static HttpRequest post(Context context, @Nullable String url, HashMap<String, String> params, RequestCacheType defaultCacheType) {
        return post(context, url, null, params, defaultCacheType);
    }

    public static HttpRequest post(Context context, @Nullable String url, HashMap<String, String> params) {
        return post(context, url, null, params, RequestCacheType.DEFAULT);
    }


    private static String buildUrlWithParams(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        }
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getValue() != null) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
        }
        if (params.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    private static HashMap<String, String> buildUrlWithHeaders(Context context, HashMap<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            headers.put(VERSION, VenvyPackageUtil.getPackageVersion(context));
        } catch (PackageManager.NameNotFoundException e) {
            VenvyLog.e(TAG, e);
        }
        headers.put(USER_AGENT, VenvyDeviceUtil.getUserAgent());
        headers.put(OS_VERSION, VenvyDeviceUtil.getOsVersion());
        headers.put(UDID, VenvyDeviceUtil.getDeviceUuid(context).toString());
        headers.put(TOKEN, "");
        headers.put(IP, VenvyDeviceUtil.getLocalIPAddress());
        headers.put(NETWORK, VenvyDeviceUtil.getNetWorkName(context));
        PlatformInfo platformInfo = Platform.instance().getPlatformInfo();
        if (platformInfo != null) {
            headers.put(SDK_VERSION, platformInfo.getSdkVersion());
            headers.put(PLATFORM_ID, platformInfo.getThirdPlatformId());
            headers.put(CYTRON_VERSION, platformInfo.getServiceVersion());
        }
        return headers;
    }
}
