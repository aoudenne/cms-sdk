package com.bazaarvoice.cms.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties (ignoreUnknown = true)
public class DecisionResponse {
    private ContextResponse context;
    private String submissionUuid;
    private String decisionAckUuid;
    private boolean moderationComplete;
    private Map<String, String> moderationStates = new HashMap<String, String>();
    private Map<String, DecisionAreaResponse> decisionAreas = new HashMap<String, DecisionAreaResponse>();
    private Map<String, String> additionalInfo = new HashMap<String, String>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContextResponse {
        private String contentType;
        private String locale;
        private String client;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DecisionAreaResponse {
        private String name;
        private String decision;
        private List<ModerationResponse> moderationResults = new ArrayList<ModerationResponse>();
        private Map<String, String> context = new LinkedHashMap<String, String>();

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
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModerationResponse {
        private String code;
        private String codeFullName;
        private String quotedText;
        private String quotedField;
        private int selectionStartIndex;
        private int selectionEndIndex;
        private String userComment;
    }
}

