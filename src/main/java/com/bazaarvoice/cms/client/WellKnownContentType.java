package com.bazaarvoice.cms.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WellKnownContentType {
    REVIEW("Review"),
    PROFILE("Profile"),
    QUESTION("Question"),
    ANSWER("Answer"),
    STORY("Story"),
    COMMENT("Comment"),
    PRODUCT_MATCH("ProductMatch");

    private final String value;

    public static WellKnownContentType fromValue(String value) {
        if (value != null) {
            for (WellKnownContentType t : WellKnownContentType.values()) {
                if (value.equals(t.getValue())) {
                    return t;
                }
            }
        }
        return null;
    }
}
