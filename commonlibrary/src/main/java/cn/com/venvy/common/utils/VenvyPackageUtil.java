package cn.com.venvy.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    /***
     * 获取assets目录下文件内容
     * @param context
     * @param assetsFile
     * @return
     */
    public static String getAssetConfigs(Context context, String assetsFile) {
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(context.getAssets().open(assetsFile));
            br = new BufferedReader(reader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getAppName(Context context) {
        String appName = null;
        try {
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            appName = info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appName;
    }


}
