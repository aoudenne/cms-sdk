package com.bazaarvoice.cms.client.exception;

import lombok.Getter;

/**
 * Indicates that the CMS API endpoint returned a response indicating an error condition.
 */
@Getter
public class CmsApiException extends CmsException {
    private int httpStatusCode;

    public CmsApiException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
