package com.bazaarvoice.cms.client;

import com.bazaarvoice.cms.client.exception.CmsApiException;
import com.bazaarvoice.cms.client.exception.CmsException;
import com.bazaarvoice.cms.client.exception.CmsNetworkException;
import com.bazaarvoice.cms.client.jersey.JerseyClientFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.Date;
import java.util.List;

public class CmsClient {
    private final Client jerseyClient;
    private final Signer signer;
    private final CmsClientConfig config;

    private static final String URL_PATH = "/moderation/api/1/submission/";

    public CmsClient(CmsClientConfig config) {
        this.config = config;
        this.signer = new Signer(config.getSecretKey());
        this.jerseyClient = JerseyClientFactory.createClient(config);
    }

    public void setDebugOn() {
        this.jerseyClient.addFilter(new LoggingFilter(System.out));
    }


    protected WebResource.Builder sign(WebResource.Builder builder,String jsonRequest) {
        String date = new Date().toString();
        String signature = null;
        if (jsonRequest!=null) {
            signature = signer.sign(date, jsonRequest);
        } else {
            signature = signer.sign(date);
        }

        return builder.header(HttpHeaderFieldNames.API_KEY, config.getApiKey())
                .header(HttpHeaderFieldNames.DATE, date)
                .header(HttpHeaderFieldNames.SIGNATURE, signature)
                .header(HttpHeaderFieldNames.PASS_KEY, config.getApiKey());
    }

    public String submitContent(Submission request)
            throws CmsException {

        try {
            String jsonRequest = JsonHelper.toJson(request);

            WebResource.Builder builder = jerseyClient.resource(config.getHostAndPort() + URL_PATH + "content")
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder, jsonRequest);
            return builder.post(String.class, jsonRequest);
        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public List<String> submitContentBatch(List<Submission> requests)
            throws CmsException {

        try {
            String jsonRequest = JsonHelper.toJson(requests);
            WebResource.Builder builder = jerseyClient.resource(config.getHostAndPort() + URL_PATH + "content/batch")
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder,jsonRequest);
            return builder.post(List.class, jsonRequest);

        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public DecisionResponse getDecision(String submissionId)
            throws CmsException {

        try {
            UriBuilder uriBuilder = jerseyClient.resource(config.getHostAndPort()).getUriBuilder();
            uriBuilder.path(URL_PATH + "content/{s}");

            WebResource.Builder builder = jerseyClient.resource(uriBuilder.build(submissionId))
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder,null);
            return builder.get(DecisionResponse.class);

        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public String cancelDecisions(List<String> submissionIds)
            throws CmsException {

        try {
            String jsonRequest = JsonHelper.toJson(submissionIds);
            WebResource.Builder builder = jerseyClient.resource(config.getHostAndPort() + URL_PATH + "content/cancel")
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder,jsonRequest);
            return builder.post(String.class, jsonRequest);
        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public DecisionResponse getNextDecision()
            throws CmsException {
        return getNextDecision(null);
    }

    public DecisionResponse getNextDecision(String accountSegment)
            throws CmsException {

        try {
            UriBuilder uriBuilder = jerseyClient.resource(config.getHostAndPort() + URL_PATH + "decision/next").getUriBuilder();
            if (accountSegment != null) {
                uriBuilder.queryParam("accountSegment", accountSegment);
            }
            WebResource.Builder builder = jerseyClient.resource(uriBuilder.build())
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder,null);
            return builder.get(DecisionResponse.class);
        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public String acknowledgeDecision(String decisionUuid, String contentSubmissionUuid)
            throws CmsException {

        try {
            UriBuilder uriBuilder = jerseyClient.resource(config.getHostAndPort() + URL_PATH + "decision").getUriBuilder();
            uriBuilder.queryParam("contentSubmissionUuid", contentSubmissionUuid);

            WebResource.Builder builder = jerseyClient.resource(uriBuilder.build())
                    .type(MediaType.APPLICATION_JSON_TYPE);
            builder = sign(builder,decisionUuid);
            return builder.post(String.class, decisionUuid);
        } catch (UniformInterfaceException e) {
            throw new CmsApiException(e.getMessage(), e.getResponse().getStatus());
        } catch (ClientHandlerException e) {
            throw new CmsNetworkException(e.getMessage(), e);
        } catch (Exception e) {
            throw new CmsException(e.getMessage(), e);
        }
    }

    public String acknowledgeDecision(DecisionResponse decision)
            throws CmsException {
        return acknowledgeDecision(decision.getDecisionAckUuid(),decision.getSubmissionUuid());
    }
}
