package com.mentor.club.model.error;

import lombok.Getter;

/**
 * HttpCallError enum
 */
public enum HttpCallError {
    DEFAULT("Task http call error"),
    INVALID_INPUT("Invalid input"),
    FAILED_TO_CREATE_TOKEN("Failed to create token"),
    FAILED_TO_SAVE_TO_DB("Failed to save to db"),
    FAILED_TO_FIND_TOKEN("Failed to find token in db"),
    SERVICE_UNAVAILABLE("Service unavailable"),
    EXTERNAL_ERROR("Error while trying to handle some external error"),
    READ_INPUT_STREAM("Error while trying to read data from an input stream"),
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