package cn.com.venvy.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.RequestFactory;

/**
 * Created by yanjiangbo on 2017/4/27.
 */

public class VenvyPackageUtil {

    public static String getPackageVersion(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionName;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getSDKVersion(Context context) {
        return "1.0.0";
    }
}
