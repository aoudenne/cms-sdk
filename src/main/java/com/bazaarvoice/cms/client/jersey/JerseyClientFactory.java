package com.bazaarvoice.cms.client.jersey;

import com.bazaarvoice.cms.client.CmsClientConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class JerseyClientFactory {

    public static Client createClient(CmsClientConfig config) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, config.getConnectTimeout());
        clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, config.getReadTimeout());

        if (config.isDisableCertAndHostValidation()) {
            configureClientToBeInsecure(clientConfig);
        }
        return Client.create(clientConfig);
    }

    private static void configureClientToBeInsecure(ClientConfig config) {
        try {
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new EveryoneHostnameVerifier(), getInsecureSSLContext()));
        } catch (Exception e) {
            throw new IllegalStateException("Exception setting up insecure client config", e);
        }
    }

    private static SSLContext getInsecureSSLContext() throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        return sc;
    }

    private static class EveryoneHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}
