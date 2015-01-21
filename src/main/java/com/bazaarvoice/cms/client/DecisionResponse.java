package com.bazaarvoice.cms.client;

import lombok.Data;
import lombok.experimental.Builder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties (ignoreUnknown = true)
public class DecisionResponse {
    public ContextResponse context;
    public String submissionUuid;
    public String decisionAckUuid;
    public boolean moderationComplete;
    public Map<String, String> moderationStates = new HashMap<String, String>();
    public Map<String, DecisionAreaResponse> decisionAreas = new HashMap<String, DecisionAreaResponse>();
    public Map<String, String> additionalInfo = new HashMap<String, String>();

    @Builder
    public DecisionResponse(ContextResponse contextResponse, String submissionUuid, String decisionAckUuid, boolean moderationComplete, Map<String, String> moderationStates,
    Map<String, DecisionAreaResponse> decisionAreas,
    Map<String, String> additionalInfo) {
        this.context = contextResponse;
        this.submissionUuid = submissionUuid;
        this.decisionAckUuid = decisionAckUuid;
        this.moderationComplete = moderationComplete;
        this.moderationStates = moderationStates;
        this.decisionAreas = decisionAreas;
        this.additionalInfo = additionalInfo;
    }

    public DecisionResponse() {}

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContextResponse {
        public String contentType;
        public String locale;
        public String client;
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DecisionAreaResponse {
        public String name;
        public String decision;
        public List<ModerationResponse> moderationResults = new ArrayList<ModerationResponse>();
        public Map<String, String> context = new LinkedHashMap<String, String>();

        public boolean isApproved() {
            return "approved".equalsIgnoreCase(decision);
        }

        public boolean isRejected() {
            return "rejected".equalsIgnoreCase(decision);
        }

        public boolean isCanceled() {
            return "canceled".equalsIgnoreCase(decision);
        }
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModerationResponse {
        public String code;
        public String codeFullName;
        public String quotedText;
        public String quotedField;
        public int selectionStartIndex;
        public int selectionEndIndex;
        public String userComment;
    }
}

