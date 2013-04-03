package com.bazaarvoice.cms.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor (access = AccessLevel.PRIVATE)
public class CmsClientConfig {
    private final String hostAndPort;
    private final String apiKey;
    private final String secretKey;
    private final boolean disableCertAndHostValidation;
    private final int connectTimeout;
    private final int readTimeout;

    public static class Builder {
        private String hostAndPort;
        private String apiKey;
        private String secretKey;
        private boolean disableCertAndHostValidation = false;
        private int connectTimeout = 30000;             // 30 seconds
        private int readTimeout = 90000;                // 90 seconds

        public Builder(String hostAndPort, String apiKey, String secretKey) {
            this.hostAndPort = hostAndPort;
            this.apiKey = apiKey;
            this.secretKey = secretKey;
        }

        public CmsClientConfig build() {
            return new CmsClientConfig(
                    hostAndPort,
                    apiKey,
                    secretKey,
                    disableCertAndHostValidation,
                    connectTimeout,
                    readTimeout);
        }

        public Builder withDisableCertAndHostValidation(boolean disableCertAndHostValidation) {
            this.disableCertAndHostValidation = disableCertAndHostValidation;
            return this;
        }

        public Builder withConnectTimeout(int connectTimeoutInMilliseconds) {
            this.connectTimeout = connectTimeoutInMilliseconds;
            return this;
        }

        public Builder withReadTimeout(int readTimeoutInMilliseconds) {
            this.readTimeout = readTimeoutInMilliseconds;
            return this;
        }
    }
}
