package cn.com.venvy;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class Platform {

    private static final String TAG = "Platform";

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
    }

    public PlatformInfo getPlatformInfo() {
        return mPlatformInfo;
    }
}
