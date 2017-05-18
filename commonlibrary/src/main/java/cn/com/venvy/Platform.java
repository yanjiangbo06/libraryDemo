package cn.com.venvy;

import android.content.Context;

import cn.com.venvy.common.image.ImageFactory;
import cn.com.venvy.common.observer.ObservableManager;
import cn.com.venvy.common.report.Report;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class Platform {

    private static Platform sPlatform;
    private PlatformInfo mPlatformInfo;

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
        Report.init();
    }

    public PlatformInfo getPlatformInfo() {
        return mPlatformInfo;
    }

    public Context getContext() {
        return mPlatformInfo.getContext();
    }

    public void onDestroy() {
        Report.onDestroy();
        ObservableManager.getDefaultObserable().removeAllObserver();
    }
}
