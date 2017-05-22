package cn.com.venvy;

import android.content.Context;

import cn.com.venvy.common.observer.ObservableManager;
import cn.com.venvy.common.report.Report;
import cn.com.venvy.common.track.TrackHelper;
import cn.com.venvy.common.utils.VenvyStatUtil;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class Platform {

    private PlatformInfo mPlatformInfo;

    public Platform (PlatformInfo platformInfo) {
        if (platformInfo != null) {
            mPlatformInfo = platformInfo;
        }
        Report.init(this);
        VenvyStatUtil.init(this);
        TrackHelper.init(this);
    }

    public PlatformInfo getPlatformInfo() {
        return mPlatformInfo;
    }

    public Context getContext() {
        return mPlatformInfo.getContext();
    }

    public void onDestroy() {
        Report.onDestroy();
        VenvyStatUtil.onDestroy();
        TrackHelper.onDestroy();
        ObservableManager.getDefaultObserable().removeAllObserver();
    }
}
