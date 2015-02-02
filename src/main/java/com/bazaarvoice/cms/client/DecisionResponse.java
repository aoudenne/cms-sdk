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
    private ContextResponse context;
    private String submissionUuid;
    private String decisionAckUuid;
    private boolean moderationComplete;
    private Map<String, String> moderationStates = new HashMap<String, String>();
    private Map<String, DecisionAreaResponse> decisionAreas = new HashMap<String, DecisionAreaResponse>();
    private Map<String, String> additionalInfo = new HashMap<String, String>();

    @Builder
    public DecisionResponse(ContextResponse context,
                            String submissionUuid,
                            String decisionAckUuid,
                            boolean moderationComplete,
                            Map<String, String> moderationStates,
                            Map<String, DecisionAreaResponse> decisionAreas,
                            Map<String, String> additionalInfo) {
        this.context = context;
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
        private String contentType;
        private String locale;
        private String client;

        public ContextResponse(String contentType,
                               String locale,
                               String client) {
            this.contentType = contentType;
            this.locale = locale;
            this.client = client;
        }

        public ContextResponse() {}
    }

    @Data
    @Builder
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

        public DecisionAreaResponse(String name,
                                    String decision,
                                    List<ModerationResponse> moderationResults,
                                    Map<String, String> context) {
            this.name = name;
            this.decision = decision;
            this.moderationResults = moderationResults;
            this.context = context;
        }

        public DecisionAreaResponse() {}
    }

    @Data
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

        public ModerationResponse(String code,
                                  String codeFullName,
                                  String quotedText,
                                  String quotedField,
                                  int selectionStartIndex,
                                  int selectionEndIndex,
                                  String userComment) {
            this.code = code;
            this.codeFullName = codeFullName;
            this.quotedText = quotedText;
            this.quotedField = quotedField;
            this.selectionStartIndex = selectionStartIndex;
            this.selectionEndIndex = selectionEndIndex;
            this.userComment = userComment;
        }

        public ModerationResponse() {}
    }
}

