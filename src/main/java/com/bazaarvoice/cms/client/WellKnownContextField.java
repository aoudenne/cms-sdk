package com.bazaarvoice.cms.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WellKnownContextField {
    Id("id"),
    ContentId("contentId"),
    ContentVersionId("contentVersionId"),
    ExternalId("externalId"),
    SubmissionTime("submissionTime"),
    ContentType("contentType"),
    ReviewRating("reviewRating"),
    ReviewId("reviewId"),
    ReviewTitle("reviewTitle"),
    ReviewText("reviewText"),
    ReviewerId("reviewerId"),
    ProductId("productId"),
    ProductExternalId("productExternalId"),
    ProductUrl("productUrl"),
    ProductDescription("productDescription"),
    ProductName("productName"),
    ProductImage("productImage"),
    CategoryId("categoryId"),
    CategoryExternalId("categoryExternalId"),
    CategoryUrl("categoryUrl"),
    CategoryName("categoryName"),
    QuestionId("questionId"),
    QuestionSummary("questionSummary"),
    QuestionDetails("questionDetails"),
    QuestionText("questionText"),
    QuestionCategory("questionCategory"),
    QuestionSubject("questionSubject"),
    StoryId("storyId"),
    StoryTitle("storyTitle"),
    StoryText("storyText");

    private final String value;

    public static WellKnownContextField fromValue(String value) {
        if (value != null) {
            for (WellKnownContextField t : WellKnownContextField.values()) {
                if (value.equals(t.getValue())) {
                    return t;
                }
            }
        }
        return null;
    }
}
