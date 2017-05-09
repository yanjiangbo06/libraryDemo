package cn.com.venvy;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class PlatformInfo {

    final String mSdkVersion;
    final String mThirdPlatformId;
    final String mServiceVersion;

    private PlatformInfo(Builder builder) {
        mSdkVersion = builder.mSdkVersion;
        mThirdPlatformId = builder.mThirdPlatformId;
        mServiceVersion = builder.mServiceVersion;
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

    public static class Builder {

        private String mSdkVersion;
        private String mThirdPlatformId;
        private String mServiceVersion;

        /**
         * 设置当前SDK发布版本号
         * @param sdkVersion
         * @return
         */
        public Builder setSdkVersion(String sdkVersion) {
            this.mSdkVersion = sdkVersion;
            return this;
        }

        /**
         * 设置对接平台ID
         * @param thirdPlatformId
         * @return
         */
        public Builder setThirdPlatform(String thirdPlatformId) {
            this.mThirdPlatformId = thirdPlatformId;
            return this;
        }

        /**
         * 设置service后台版本号
         * @param serviceVersion
         * @return
         */
        public Builder setServiceVersion(String serviceVersion) {
            this.mServiceVersion = serviceVersion;
            return this;
        }

        public PlatformInfo builder() {
            return new PlatformInfo(this);

        }
    }
}
