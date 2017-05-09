package cn.com.venvy;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class Platform {

    private static final String TAG = "Platform";

    private static Platform sPlatform;
    private PlatformInfo mPlatformInfo;
    private Context mApplicationContext;

    public synchronized static Platform instance() {
        if (sPlatform == null) {
            sPlatform = new Platform();
        }
        return sPlatform;
    }

    public void init(PlatformInfo platformInfo) {
        if (platformInfo != null) {
            mPlatformInfo = platformInfo;
        }
    }

    public PlatformInfo getPlatformInfo() {
        return mPlatformInfo;
    }

    public void setVenvyApplication(IVenvyApplication application) {
        if (application != null) {
            mApplicationContext = application.getApplication();
        }
    }

    public Context getContext() {
        return mApplicationContext;
    }
}
