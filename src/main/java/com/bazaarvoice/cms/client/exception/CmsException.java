package com.bazaarvoice.cms.client.exception;

/**
 * Root of the CMS API exception hierarchy
 */
public class CmsException extends Exception {

    public CmsException(String message) {
        super(message);
    }

    public CmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
