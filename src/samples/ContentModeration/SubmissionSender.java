import com.bazaarvoice.cms.client.CmsClient;
import com.bazaarvoice.cms.client.CmsClientConfig;
import com.bazaarvoice.cms.client.Submission;
import com.bazaarvoice.cms.client.WellKnownContentField;
import com.bazaarvoice.cms.client.WellKnownContentType;
import com.bazaarvoice.cms.client.WellKnownContextField;
import com.bazaarvoice.cms.client.exception.CmsException;

public class SubmissionSender {

    private CmsClient cmsClient;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Your ApiKey must be passed in as an argument");
            return;
        }

        SubmissionSender example = new SubmissionSender("https://sandbox.api.bazaarvoice.com",
                args[0],
                "NotUsed-testSecretKey");

        try {
            example.postSubmission();
        } catch (CmsException e) {
            System.out.println("Problem sending submission");
            e.printStackTrace();
        }

    }

    public SubmissionSender(String url, String apiKey, String apiSecret) {

        CmsClientConfig config = new CmsClientConfig.Builder(url,apiKey,apiSecret)
                .withDisableCertAndHostValidation(true)
                .build();
        cmsClient = new CmsClient(config);
        cmsClient.setDebugOn();
    }

    public void postSubmission()
            throws CmsException {
        Submission submission = createExampleReviewSubmission();
        String submissionUuid = cmsClient.submitContent(submission);
        System.out.println("Your submission reference UUID is "+ submissionUuid);
    }

    Submission createExampleReviewSubmission() {
        Submission.SubmissionContext submissionContext
                = new Submission.SubmissionContext("Lego", "Review", "en_US");

        return new Submission(submissionContext)
                .withDecisionArea("content",
                        new Submission.DecisionArea()
                                .withContentType(WellKnownContentType.REVIEW)
                                .withContextField(WellKnownContextField.Id, "10517557")
                                .withContextField(WellKnownContextField.ProductDescription, "Indiana Jones has come to a temple deep in the jungle in search of an ancient idol. But can he avoid the collapsing walls, shooting arrow traps, slithering snakes, spiked walls and a massive boulder rolling right for him? It's all out action, Indy style! Includes Indiana Jones with hat, whip and shoulder bag, Belloq, Satipo and pilot minifigures! Help Indiana Jones take the gold idol before the statue falls, get past the flick-fire arrows, swing over the pit using his whip, run from the rolling boulder and past the swinging swords to safety! Set includes 6\" (15cm) long plane with snake inside, just like the scene from the movie! Features and details include skull rocks, bat, spider, spiderweb, skeletons, gold coins and more! Temple measures a total of 21\" (53cm) long!")
                                .withContextField(WellKnownContextField.ProductExternalId, "7623")
                                .withContextField(WellKnownContextField.ProductId, "34274353")
                                .withContextField(WellKnownContextField.ProductName, "LEGO Indiana Jones: Temple Escape")
                                .withContextField(WellKnownContextField.ProductUrl, "http://shop.lego.com/product.asp?p=7623&CMP=AFC-BP6648365778&HQS=7623")
                                .withContextField("ReviewerID", "4852986")
                                .withModeratableField(WellKnownContentField.ReviewText, "this set is great! I love it! here I'll show you my likes and dislikes.1 very detailed.2 its very sturdy.(never falls apart when transported.)3 the closing door never closes all the way.4 EVERY TIME I PLAY WITH IT the leaves fall off! but I still recomend it to every one. but its costs TO much for a set that small.I give it 5 bricks.")
                                .withModeratableField(WellKnownContentField.ReviewTitle, "AWESOME SET!!!")
                                .withModeratableField(WellKnownContentField.Rating, "5")
                                .withModeratableField("someUnicodeText", "It is amazing in every way possible😍😍😍")
                                .withModeratablePhoto("https://secure.gravatar.com/avatar/da742a6f2dde2b9d231436c89bba4ff8?s=420&d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png", "Joe's Gravatar")
                );
    }
}
