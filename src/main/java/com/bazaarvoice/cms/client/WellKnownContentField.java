package com.bazaarvoice.cms.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WellKnownContentField {
    ReviewTitle("title"),
    ReviewText("reviewText"),
    Pros("pros"),
    Cons("cons"),
    Rating("rating"),
    Tag("tag"),
    StoryTitle("storyTitle"),
    StoryText("storyText"),
    QuestionSummary("questionSummary"),
    QuestionDetails("questionDetails"),
    AnswerText("answerText"),
    CommentTitle("commentTitle"),
    CommentText("commentText"),
    Nickname("nickname"),
    Location("location"),
    ExternalId("productId"),
    Client("client"),
    Confidence("confidence");


    private final String value;

    public static WellKnownContentField fromValue(String value) {
        if (value != null) {
            for (WellKnownContentField t : WellKnownContentField.values()) {
                if (value.equals(t.getValue())) {
                    return t;
                }
            }
        }
        return null;
    }
}
