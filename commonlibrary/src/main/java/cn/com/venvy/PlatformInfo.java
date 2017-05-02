package cn.com.venvy;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public class PlatformInfo {

    final String sdkVersion;
    final String thirdPlatformId;
    final String serviceVersion;

    private PlatformInfo(Builder builder) {
        sdkVersion = builder.sdkVersion;
        thirdPlatformId = builder.thirdPlatformId;
        serviceVersion = builder.serviceVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getThirdPlatformId() {
        return thirdPlatformId;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public static class Builder {

        private String sdkVersion;
        private String thirdPlatformId;
        public String serviceVersion;

        /**
         * 设置当前SDK发布版本号
         * @param sdkVersion
         * @return
         */
        public Builder setSdkVersion(String sdkVersion) {
            this.sdkVersion = sdkVersion;
            return this;
        }

        /**
         * 设置对接平台ID
         * @param thirdPlatformId
         * @return
         */
        public Builder setThirdPlatform(String thirdPlatformId) {
            this.thirdPlatformId = thirdPlatformId;
            return this;
        }

        /**
         * 设置service后台版本号
         * @param serviceVersion
         * @return
         */
        public Builder setServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public PlatformInfo builder() {
            return new PlatformInfo(this);

        }
    }
}
