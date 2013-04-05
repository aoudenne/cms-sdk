import com.bazaarvoice.cms.client.CmsClient;
import com.bazaarvoice.cms.client.CmsClientConfig;
import com.bazaarvoice.cms.client.DecisionResponse;
import com.bazaarvoice.cms.client.exception.CmsException;

public class DecisionPoller {

    private CmsClient cmsClient;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Your ApiKey must be passed in as an argument");
            return;
        }

        DecisionPoller example = new DecisionPoller("https://sandbox.api.bazaarvoice.com",
                args[0],
                "testSecretKey");


        while (true) {
            try {
                example.pollForFirstAvailableModerationDecision();
            } catch (Exception e) {
                System.out.println("Problem polling for moderation decisions");
                e.printStackTrace();
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e1) {}
            }
        }
    }

    public DecisionPoller(String url, String apiKey, String apiSecret) {

        CmsClientConfig config = new CmsClientConfig.Builder(url,apiKey,apiSecret)
                .withDisableCertAndHostValidation(true)
                .build();
        cmsClient = new CmsClient(config);
        cmsClient.setDebugOn();
    }

    public void pollForFirstAvailableModerationDecision()
            throws CmsException {

        // Blocks up to 20 seconds waiting for an available decision
        DecisionResponse decision = cmsClient.getNextDecision();
        System.out.println(decision);

        // See if a moderation decision was available.
        if (decision.getSubmissionUuid() != null) {
            System.out.println(String.format("Submission %s had the following moderation result",decision.getSubmissionUuid()));
            for (DecisionResponse.DecisionAreaResponse response : decision.getDecisionAreas().values()) {
                if (response.isApproved()) {
                    System.out.println("Approved!");
                } else {
                    System.out.println("Not Approved!");
                }

                // Get information about any tags that were applied to the submission
                for (DecisionResponse.ModerationResponse tagInfo : response.getModerationResults()) {
                    System.out.println("The content was tagged with "+tagInfo.getCode());
                }
            }

            // Acknowledge or same decision will be returned again by cmsClient.getNextDecision()
            cmsClient.acknowledgeDecision(decision);

        } else {
            System.out.println("No Moderation results available");
        }

    }

}