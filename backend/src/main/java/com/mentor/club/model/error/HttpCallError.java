package com.mentor.club.model.error;

import lombok.Getter;

/**
 * HttpCallError enum
 */
public enum HttpCallError {
    DEFAULT("Task http call error"),
    INVALID_INPUT("Invalid input"),
    SERVICE_UNAVAILABLE("Service unavailable"),
    EXTERNAL_ERROR("Error while trying to handle some external error"),
    READ_INPUTSTREAM("Error while trying to read data from an inputstream"),
    DESERIALIZE("Error while trying to deserialize data"),
    SERIALIZE("Error while trying to serialize data");

    @Getter
    private final String description;

    /**
     * HttpCallError constructor
     *
     * @param description HttpCallError description
     */
    HttpCallError(String description) {
        this.description = description;
    }
}