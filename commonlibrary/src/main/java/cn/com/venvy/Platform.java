package cn.com.venvy;

import android.content.Context;

import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.observer.ObservableManager;
import cn.com.venvy.common.report.Report;
import cn.com.venvy.common.track.TrackHelper;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class Platform {

    private PlatformInfo mPlatformInfo;
    private IRequestConnect mConnect;
    private Report mReport;
    private TrackHelper mTrackHelper;

    public Platform(PlatformInfo platformInfo) {
        if (platformInfo != null) {
            mPlatformInfo = platformInfo;
        }
        mReport = new Report();
        mTrackHelper = new TrackHelper();
        mReport.init(this);
        mTrackHelper.init(this);
    }

    public PlatformInfo getPlatformInfo() {
        return mPlatformInfo;
    }

    public Context getContext() {
        return mPlatformInfo.getContext();
    }

    public Report getReport() {
        return mReport;
    }

    public TrackHelper getTrackHelper() {
        return mTrackHelper;
    }

    public IRequestConnect getRequestConnect() {
        if (mConnect == null) {
            mConnect = RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP, this);
        }
        return mConnect;
    }

    public void onDestroy() {
        mReport.onDestroy();
        mTrackHelper.onDestroy();
        ObservableManager.getDefaultObserable().removeAllObserver();
    }
}
