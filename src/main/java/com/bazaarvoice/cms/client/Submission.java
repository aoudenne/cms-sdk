package com.bazaarvoice.cms.client;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@ToString
/**
 * This represents a submission to CMS2 for moderation.
 *
 * Included are a nullable accountSegment, a submission context, and a map of name -> decision areas.
 */
public class Submission {
    private final String accountSegment;
    private final SubmissionContext context;
    private final Map<String, DecisionArea> decisionAreas;
    private final Map<String, String> additionalInfo;

    public Submission(String accountSegment, SubmissionContext context) {
        this(accountSegment, context, new LinkedHashMap<String, DecisionArea>(), new LinkedHashMap<String, String>());
    }

    public Submission(SubmissionContext context) {
        this(null, context, new LinkedHashMap<String, DecisionArea>(), new LinkedHashMap<String, String>());
    }

    @JsonCreator
    public Submission(@JsonProperty("accountSegment") String accountSegment,
                      @JsonProperty("context") SubmissionContext context,
                      @JsonProperty("decisionAreas") Map<String, DecisionArea> decisionAreas,
                      @JsonProperty("additionalInfo") Map<String, String> additionalInfo) {
        this.accountSegment = accountSegment;
        this.context = context;
        this.decisionAreas = decisionAreas;
        this.additionalInfo = additionalInfo;
    }

    public Submission withDecisionArea(String key, DecisionArea decisionArea) {
        decisionAreas.put(key, decisionArea);
        return this;
    }

    public Map<String, DecisionArea> getDecisionAreas() {
        return Collections.unmodifiableMap(decisionAreas);
    }

    public Map<String, String> getAdditionalInfo() {
        return Collections.unmodifiableMap(additionalInfo);
    }

    public Submission withAdditionalInfo(String key, String value) {
        additionalInfo.put(key, value);
        return this;
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties (ignoreUnknown = true)
    /**
     * The context of the submission includes a few well known fields for bucketing and assigning work in CMS2.
     */
    public static class SubmissionContext {
        private String client;
        private final String contentType;
        private final String locale;

        @JsonCreator
        public SubmissionContext(@JsonProperty("client") String client,
                                 @JsonProperty("contentType") String contentType,
                                 @JsonProperty("locale") String locale) {
            this.client = client;
            this.contentType = contentType;
            this.locale = locale;
        }
    }

    /**
     * A decision
     */
    @ToString (of = {"context", "info"})
    public static class DecisionArea {
        private static final String PHOTO_SUFFIX = "-PHOTO-URL";
        private static final String VIDEO_SUFFIX = "-VIDEO-URL";
        private static final String BRIGHTCOVE_VIDEO_SUFFIX = "-BRIGHTCOVE-VIDEO";
        private static final String GENERIC_URL_SUFFIX = "-GENERIC-URL";

        private final Map<String, String> context;
        private final Map<String, String> info;

        private int photoCount = 0;
        private int videoCount = 0;
        private int urlCount = 0;

        public DecisionArea() {
            this(new LinkedHashMap<String, String>(), new LinkedHashMap<String, String>());
        }

        @JsonCreator
        public DecisionArea(@JsonProperty("context") Map<String, String> context,
                            @JsonProperty("info") Map<String, String> info) {
            this.context = context;
            this.info = info;
        }

        public DecisionArea withContentType(WellKnownContentType contentType) {
            return withContentType(contentType.getValue());
        }

        public DecisionArea withContentType(String contentType) {
            context.put(WellKnownContextField.ContentType.getValue(), contentType);
            return this;
        }

        public DecisionArea withContextField(String key, String value) {
            context.put(key, value);
            return this;
        }

        public DecisionArea withContextField(WellKnownContextField key, String value) {
            return this.withContextField(key.getValue(), value);
        }

        public DecisionArea withModeratableField(String key, String value) {
            info.put(key, value);
            return this;
        }

        public DecisionArea withModeratableField(WellKnownContentField key, String value) {
            return this.withModeratableField(key.getValue(), value);
        }

        public DecisionArea withModeratablePhoto(String url, String caption) {
            ++photoCount;
            String urlKey = "photo" + photoCount + "Url";
            String captionKey = "photo" + photoCount + "Caption";
            return withModeratablePhoto(urlKey, url, captionKey, caption);
        }

        public DecisionArea withModeratablePhoto(String urlKey, String url, String captionKey, String caption) {
            return addUrlComponents(urlKey + PHOTO_SUFFIX, url, captionKey, caption);
        }

        public DecisionArea withModeratableVideo(String url, String caption) {
            ++videoCount;
            String urlKey = "video" + videoCount + "Url";
            String captionKey = "video" + videoCount + "Caption";
            return withModeratableVideo(urlKey, url, captionKey, caption);
        }

        public DecisionArea withModeratableVideo(String urlKey, String url, String captionKey, String caption) {
            return addUrlComponents(urlKey + VIDEO_SUFFIX, url, captionKey, caption);
        }

        public DecisionArea withModeratableBrightcoveVideo(String videoKey, String playerId, String publisherId, String videoId, String captionKey, String caption)
                throws IOException {
            Map<String, String> attributes = new LinkedHashMap<String, String>();
            attributes.put("playerId", playerId);
            attributes.put("publisherId", publisherId);
            attributes.put("videoId", videoId);

            return this
                    .withModeratableField(videoKey + BRIGHTCOVE_VIDEO_SUFFIX, JsonHelper.toJson(attributes))
                    .withModeratableField(captionKey, caption);
        }

        public DecisionArea withModeratableUrl(String url, String caption) {
            ++urlCount;
            String urlKey = "url" + urlCount;
            String captionKey = urlKey + "Caption";
            return withModeratableUrl(urlKey, url, captionKey, caption);
        }

        public DecisionArea withModeratableUrl(String urlKey, String url, String captionKey, String caption) {
            return addUrlComponents(urlKey + GENERIC_URL_SUFFIX, url, captionKey, caption);
        }

        private DecisionArea addUrlComponents(String urlKey, String url, String captionKey, String caption) {
            return this
                    .withModeratableField(urlKey, url)
                    .withModeratableField(captionKey, caption);
        }

        public Map<String, String> getContext() {
            return Collections.unmodifiableMap(context);
        }

        public Map<String, String> getInfo() {
            return Collections.unmodifiableMap(info);
        }
    }
}
