package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class InternalResponse {
    @Getter
    @Setter
    @JsonProperty("json")
    private Object json;

    @Getter
    @Setter
    @JsonProperty("status")
    private HttpStatus status;
}
