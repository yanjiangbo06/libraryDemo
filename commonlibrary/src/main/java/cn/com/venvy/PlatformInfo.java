package cn.com.venvy;

import android.content.Context;
import android.os.Build;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class PlatformInfo {

    final String mSdkVersion;
    final String mThirdPlatformId;
    final String mServiceVersion;
    final String mBuId;
    final String mAppKey;
    final Context context;

    private PlatformInfo(Builder builder) {
        mSdkVersion = builder.mSdkVersion;
        mThirdPlatformId = builder.mThirdPlatformId;
        mServiceVersion = builder.mServiceVersion;
        mBuId = builder.mBuId;
        mAppKey = builder.mAppKey;
        context = builder.context;
    }

    public String getSdkVersion() {
        return mSdkVersion;
    }

    public String getThirdPlatformId() {
        return mThirdPlatformId;
    }

    public String getServiceVersion() {
        return mServiceVersion;
    }

    public String getBuId() {
        return mBuId;
    }

    public String getAppKey() {
        return mAppKey;
    }

    public Context getContext() {
        return context;
    }

    public static class Builder {

        private String mSdkVersion;
        private String mThirdPlatformId;
        private String mServiceVersion;
        private String mBuId;
        private String mAppKey;
        private Context context;

        /**
         * 设置当前SDK发布版本号
         *
         * @param sdkVersion
         * @return
         */
        public Builder setSdkVersion(String sdkVersion) {
            this.mSdkVersion = sdkVersion;
            return this;
        }

        /**
         * 设置对接平台标识
         *
         * @param thirdPlatformId
         * @return
         */
        public Builder setThirdPlatform(String thirdPlatformId) {
            this.mThirdPlatformId = thirdPlatformId;
            return this;
        }

        /**
         * 设置service后台版本号
         *
         * @param serviceVersion
         * @return
         */
        public Builder setServiceVersion(String serviceVersion) {
            this.mServiceVersion = serviceVersion;
            return this;
        }

        /**
         * 设置业务标识
         *
         * @return
         */
        public Builder setBuId(String buId) {
            this.mBuId = buId;
            return this;
        }

        public Builder setAppKey(String appKey) {
            this.mAppKey = appKey;
            return this;
        }

        public Builder setApplicationContext(Context context) {
            this.context = context.getApplicationContext();
            return this;
        }

        public PlatformInfo builder() {
            return new PlatformInfo(this);

        }
    }
}
