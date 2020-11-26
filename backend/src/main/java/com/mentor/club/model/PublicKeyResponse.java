package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class PublicKeyResponse {
    @Getter
    @Setter
    @JsonProperty("public_key")
    private String public_key;
}
