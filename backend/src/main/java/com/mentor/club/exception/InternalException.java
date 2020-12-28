package com.mentor.club.exception;

import org.springframework.http.HttpStatus;

import com.mentor.club.model.error.HttpCallError;

import lombok.Getter;

public class InternalException extends RuntimeException {
    private static final String DEFAULT_SERVICE_CODE = "USER_SERVICE";
    private static final String UNKNOWN_CODE = "UNKNOWN";

    /**
     * HttpStatus code that should be used when returning the exception as HttpResponse
     */
    @Getter
    private final int statusCode;

    /**
     * HttpStatus description that should be used when returning the exception as HttpResponse
     */
    @Getter
    private final String statusDescription;

    /**
     * Description of the error that occurred
     */
    @Getter
    private final String errorDescription;

    /**
     * Code indicating in which service the error occurred
     * For a list of all service codes see: TODO
     */
    @Getter
    private final String serviceCode;

    /**
     * Code indicating in which endpoint the error occurred
     * For a list of all endpoint codes see: TODO
     */
    @Getter
    private final String endpointCode;

    /**
     * Id indicating the type of error that occurred
     * For a list of all possible error id's see: TODO
     */
    @Getter
    private final String errorId;

    /**
     * InternalException constructor
     *
     * @param status HttpStatus code that should be used when returning the exception as HttpResponse
     * @param error Description of the error that occurred
     */
    public InternalException(HttpStatus status, HttpCallError error) {
        this.statusCode = status.value();
        this.statusDescription = status.getReasonPhrase();
        this.errorDescription = error.getDescription();
        this.serviceCode = DEFAULT_SERVICE_CODE;
        this.endpointCode = UNKNOWN_CODE;
        this.errorId = Integer.toString(error.ordinal());
    }

    /**
     * InternalException constructor
     *
     * @param status HttpStatus code that should be used when returning the exception as HttpResponse
     * @param error Description of the error that occurred
     * @param errorDescriptionDetails Descriptive details of the error that occurred
     */
    public InternalException(HttpStatus status, HttpCallError error, String errorDescriptionDetails) {
        this.statusCode = status.value();
        this.statusDescription = status.getReasonPhrase();
        this.errorDescription = createErrorDescription(error.getDescription(), errorDescriptionDetails);
        this.serviceCode = DEFAULT_SERVICE_CODE;
        this.endpointCode = UNKNOWN_CODE;
        this.errorId = Integer.toString(error.ordinal());
    }

    private static String createErrorDescription(String description, String details) {
        return description + " : " + details;
    }
}