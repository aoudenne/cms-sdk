package com.bazaarvoice.cms.client;

import com.bazaarvoice.cms.client.exception.CmsException;
import junit.framework.Assert;

public class DecisionGetter {
    public static void main(String[] args) {
        try {
            // Create the CMS client
            CmsClientConfig config = new CmsClientConfig.Builder("https://localhost:8943", "testApiKey", "testSecretKey")
                    .withDisableCertAndHostValidation(true)
                    .build();
            CmsClient cmsClient = new CmsClient(config);

            // Get the next decision
            DecisionResponse nextDecision = cmsClient.getNextDecision("TestSegment");
            Assert.assertNotNull(nextDecision);
            Assert.assertNotNull(nextDecision.getSubmissionUuid());
            System.out.println("Received decision: " + nextDecision);

            // Acknowledge that the decision was received
            cmsClient.acknowledgeDecision(nextDecision.getDecisionAckUuid(), nextDecision.getSubmissionUuid());
            System.out.println("Acknowledged decision");

        } catch (CmsException e ) {
            Assert.fail(e.toString());
        }
    }
}
