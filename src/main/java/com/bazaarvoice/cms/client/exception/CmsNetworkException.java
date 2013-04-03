package com.bazaarvoice.cms.client.exception;

/**
 * Indicates that a network error occurred while trying to connect to the CMS API endpoint.
 *
 * Examples: the API endpoint is down, the API request timed-out, etc.
 */
public class CmsNetworkException extends CmsException {

    public CmsNetworkException(String message) {
        super(message);
    }

    public CmsNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
