package com.mentor.club.exception;

import org.apache.http.HttpResponse;

import lombok.Getter;

/**
 * Exception used to indicate that something went wrong during a call to an external service.
 * The response is the (not-OK) response returned by the external service.
 */
public class ExternalHttpCallException extends RuntimeException {
    /**
     * The response containing the error that occurred in an external service
     */
    @Getter
    private final transient HttpResponse response;

    /**
     * ExternalHttpCallException constructor
     *
     * @param response The response containing the error that occurred in an external service
     */
    public ExternalHttpCallException(HttpResponse response) {
        this.response = response;
    }
}