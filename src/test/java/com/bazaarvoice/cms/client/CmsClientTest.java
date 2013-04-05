package com.bazaarvoice.cms.client;

import com.bazaarvoice.cms.client.exception.CmsApiException;
import com.bazaarvoice.cms.client.exception.CmsException;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CmsClientTest {
    private static final String HOST_AND_PORT = "https://sandbox.api.bazaarvoice.com";
    private static final String API_KEY = "nwnaumagavkqesvyaqtsy6tw";
    private static final String SECRET_KEY = "testSecretKey";

    private CmsClient cmsClient;

    @Before
    public void setup() {
        CmsClientConfig config = new CmsClientConfig.Builder(HOST_AND_PORT, API_KEY, SECRET_KEY)
                .withDisableCertAndHostValidation(true)
                .build();
        cmsClient = new CmsClient(config);
        cmsClient.setDebugOn();
    }

    @Test
    public void testModerationSubmission() throws CmsException {
        Submission request = createSubmission();
        String submissionId = cmsClient.submitContent(request);
        Assert.assertNotNull(submissionId);
        Assert.assertFalse(submissionId.isEmpty());
    }

    @Test
    public void testModerationSubmissionBatch() {
        try {
            Submission submission = createSubmission();
            List<Submission> requests = Lists.newArrayList();
            for (int i=0;i<5;i++) {
                requests.add(submission);
            }

            List<String> submissionUuids = cmsClient.submitContentBatch(requests);

            Assert.assertEquals(5,submissionUuids.size());

        } catch (CmsException e) {
            Assert.fail(e.toString());
        }
    }


    /*
     * TODO - add a debug option for a submission to immediately have a moderation decision
     */
    @Test
    public void testModerationResultAndAck() throws CmsException {
        Submission request = createSubmission();
        String submissionId = cmsClient.submitContent(request);
        DecisionResponse decision = cmsClient.getNextDecision(request.getAccountSegment());
        Assert.assertNotNull(decision);

        cmsClient.acknowledgeDecision(decision);
    }

    @Test
    public void testCurrentModerationState() throws CmsException {
        Submission request = createSubmission();
        String submissionId = cmsClient.submitContent(request);

        // Wait for some decision information to be ready
        DecisionResponse decision = null;
        int i = 0;
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }
            try {
                decision = cmsClient.getCurrentInfoForDebugging(submissionId);
                Assert.assertNotNull(decision);
                Assert.assertNotNull(decision.getSubmissionUuid());
                break;
            } catch (CmsException e) { }
            if (i++ > 50) {
                break;
            }
        }

        Assert.assertNotNull(decision);
    }

    @Test
    public void testCancelModerationSubmission() throws CmsException {
        Submission request = createSubmission();
        String submissionId = cmsClient.submitContent(request);
        Assert.assertEquals("SUCCESS",cmsClient.cancelDecisions(Lists.newArrayList(submissionId)));
    }

    @Test
    public void testUnauthorizedApiCall() {
        CmsClientConfig config = new CmsClientConfig.Builder(HOST_AND_PORT, "bogus_key", SECRET_KEY)
                .withDisableCertAndHostValidation(true)
                .build();
        cmsClient = new CmsClient(config);

        try {
            Submission request = createSubmission();
            String submissionId = cmsClient.submitContent(request);
            Assert.fail("Expected a CmsApiException");
        } catch (CmsApiException e) {
           Assert.assertEquals(403,e.getHttpStatusCode());
        } catch (CmsException e ) {
            Assert.fail("Expected a CmsApiException instead of this: " + e.toString());
        }
    }

    private Submission createSubmission() {
        Submission.SubmissionContext cmsSubmissionContext
                = new Submission.SubmissionContext("TestClient", "Review", "en_US");

        return new Submission("TestSegment", cmsSubmissionContext)
                .withAdditionalInfo("AdditionalContext", "AdditionalValue")

                .withDecisionArea("content",
                        new Submission.DecisionArea()
                                .withContentType(WellKnownContentType.REVIEW)
                                .withContextField("text", "some text")
                                .withModeratableField("some moderatable field", "some moderatable field's value that could be long")
                                .withModeratableField("another", "thing")
                                .withModeratableField("and", "another")
                                .withModeratablePhoto("http://placekitten.com/290/390", "gross cat")
                                .withModeratableUrl("http://placekitten.com/250/350", "gross url")
                )
                .withDecisionArea("profile",
                        new Submission.DecisionArea()
                                .withContentType(WellKnownContentType.PROFILE)
                                .withContextField("another key", "another value")
                                .withModeratableField("zounds", "foo")
                );
    }
}